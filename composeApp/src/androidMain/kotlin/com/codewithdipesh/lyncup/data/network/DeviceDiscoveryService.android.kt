package com.codewithdipesh.lyncup.data.network

import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.ServiceAnnouncement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketTimeoutException

actual class DeviceDiscoveryService {
    private var isDiscovering = false
    private var listenerJob: Job? = null
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
    }

    actual fun stopDiscovery() {
        isDiscovering = false
        listenerJob?.cancel()
        listenerJob = null
        discoveredDevices.clear()
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
                       if (serviceAnnouncement.deviceId != SharedPreference.getOrCreateDeviceId()) {
                           val device = Device(
                               id = serviceAnnouncement.deviceId,
                               name = serviceAnnouncement.deviceName,
                               ip = packet.address.hostAddress ?: "",
                               port = serviceAnnouncement.port,
                               deviceType = serviceAnnouncement.deviceType,
                               isConnected = true,
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