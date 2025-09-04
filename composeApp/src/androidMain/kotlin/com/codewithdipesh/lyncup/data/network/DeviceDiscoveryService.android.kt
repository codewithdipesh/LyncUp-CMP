package com.codewithdipesh.lyncup.data.network

import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.DeviceType
import com.codewithdipesh.lyncup.domain.model.ServiceAnnouncement
import com.codewithdipesh.lyncup.getPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

actual class DeviceDiscoveryService actual constructor(){
    private var isDiscovering = false
    private var listenerJob: Job? = null
    private var discoveryJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val discoveredDevices = mutableMapOf<String, Device>()


    actual fun startDiscovery(onDevicesFound: (List<Device>) -> Unit) {
        if (isDiscovering) return

        isDiscovering = true
        discoveredDevices.clear()

        //start listener first
        listenerJob = scope.launch {
            startListener(onDevicesFound)
        }
        //start broadcast
        discoveryJob = scope.launch {
            while (isDiscovering && isActive) {
                broadcastDiscoveryMessage()
            }
        }
    }

    actual fun stopDiscovery() {
        isDiscovering = false
        listenerJob?.cancel()
        listenerJob = null
        discoveryJob?.cancel()
        discoveryJob = null
        discoveredDevices.clear()
    }

    private suspend fun broadcastDiscoveryMessage() {
        withContext(Dispatchers.IO) {
            var socket : DatagramSocket? = null
            try {
                socket = DatagramSocket()
                socket.broadcast = true

                val broadcastMessage = ServiceAnnouncement(
                    deviceId = SharedPreference.getOrCreateDeviceId(),
                    deviceName = getPlatform().name,
                    deviceType = DeviceType.DESKTOP,
                    port = 8888
                )
                val messageJson = Json.encodeToString(broadcastMessage)
                val data = messageJson.toByteArray()

                val broadcastAddresses = listOf(
                    InetAddress.getByName("255.255.255.255"),
                    InetAddress.getByName("192.168.1.255"),
                    InetAddress.getByName("192.168.0.255")
                )

                broadcastAddresses.forEach { address ->
                    try {
                        val packet = DatagramPacket(data, data.size, address, 8888)
                        socket.send(packet)
                    } catch (e: Exception) {
                        // Continue with next address
                    }
                }

            }  catch (e: Exception) {
                println("Broadcast error: ${e.message}")
            } finally {
                socket?.close()
            }
        }
    }

    private suspend fun startListener(onDevicesFound: (List<Device>) -> Unit){
        withContext(Dispatchers.IO) {
            var socket : DatagramSocket? = null
            try {
                socket = DatagramSocket(8888)
                socket.soTimeout = 2000 //2 sec

                while(isDiscovering && isActive) {
                   try {
                       val buffer = ByteArray(1024)
                       val packet = DatagramPacket(buffer, buffer.size)
                       socket.receive(packet)

                       val message = String(packet.data, 0, packet.length)

                       val serviceAnnouncement = Json.decodeFromString<ServiceAnnouncement>(message)

                       //avoid own device
                       if (serviceAnnouncement.deviceId != SharedPreference.getOrCreateDeviceId() &&
                           serviceAnnouncement.deviceType != DeviceType.ANDROID) {
                           val device = Device(
                               id = serviceAnnouncement.deviceId,
                               name = serviceAnnouncement.deviceName,
                               ip = packet.address.hostAddress ?: "",
                               port = serviceAnnouncement.port,
                               deviceType = serviceAnnouncement.deviceType,
                               isConnected = false,
                               lastSeen = System.currentTimeMillis()
                           )

                           discoveredDevices[device.id] = device

                           //clean old devices
                           val now = System.currentTimeMillis()
                           discoveredDevices.values.removeAll {
                               now - it.lastSeen > 20_000
                           }
                           onDevicesFound(discoveredDevices.values.toList())
                       }
                   }catch (e: SocketTimeoutException){
                       //normal timeout -> continue
                       val now = System.currentTimeMillis()
                       val sizeBefore = discoveredDevices.size
                       discoveredDevices.values.removeAll {
                           now - it.lastSeen > 20_000
                       }
                       if (sizeBefore != discoveredDevices.size) {
                           onDevicesFound(discoveredDevices.values.toList())
                       }
                   }catch (e: Exception) {
                       if (isDiscovering) {
                           println("Listener error: ${e.message}")
                       }
                   }
                }
            } catch (e: Exception) {
                println("Failed to start listener: ${e.message}")
            } finally {
                socket?.close()
            }
        }
    }
}