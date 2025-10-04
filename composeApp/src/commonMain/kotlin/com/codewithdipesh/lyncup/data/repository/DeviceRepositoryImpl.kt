package com.codewithdipesh.lyncup.data.repository

import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.data.network.DeviceDiscoveryService
import com.codewithdipesh.lyncup.data.network.SocketManager
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.HandShake
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

 class DeviceRepositoryImpl(
    private val discoveryService : DeviceDiscoveryService,
    private val socketManager: SocketManager,
    private val sharedPref: SharedPreference
): DeviceRepository {

    private val _deviceFlow = MutableStateFlow<List<Device>>(emptyList())
    override val deviceFlow = _deviceFlow.asStateFlow()

    private var connectedDevice: Device? = null

    override suspend fun startDiscovery() {
        _deviceFlow.value = emptyList()
        discoveryService.startDiscovery { devices ->
            val updatedDevices = devices.map { device ->
                if(device.id == connectedDevice?.id ) {
                    device.copy(isConnected = true)
                } else {
                    device.copy(isConnected = false)
                }
            }
            _deviceFlow.value = updatedDevices
        }
    }

    override suspend fun stopDiscovery() {
        discoveryService.stopDiscovery()
    }

    override suspend fun connectToDevice(device: Device): Boolean {
        //prev device disconnection
        connectedDevice?.let { disconnectFromDevice(it) }

        val connected = socketManager.connectToDevice(device)
        if(connected) {
            connectedDevice = device
            sharedPref.setConnectedDevice(device)
            println("Device connected: ${device.name}")
            updateDeviceConnectionStatus(device,true)
        }
        return connected
    }

    private fun updateDeviceConnectionStatus(device: Device, isConnected: Boolean) {
        val currentDevices = _deviceFlow.value.toMutableList()
        val index = currentDevices.indexOfFirst { it.id == device.id }
        if(index != -1){
            currentDevices[index] = currentDevices[index].copy(isConnected = isConnected)
            _deviceFlow.value = currentDevices
        }
    }

    override suspend fun disconnectFromDevice(device: Device): Boolean {
        socketManager.disconnect()
        if(connectedDevice?.id == device.id){
            connectedDevice = null
            sharedPref.setConnectedDevice(null)
        }
        updateDeviceConnectionStatus(device,false)
        return true
    }

    override suspend fun startServer(
        onRequest: (HandShake, (Boolean) -> Unit) -> Unit,
        onClipboardReceived: (ClipBoardData) -> Unit,
        onError: () -> Unit
    ): Boolean {
        return socketManager.startServer(
            onRequest,
            onClipboardReceived,
            onError
        )
    }

    override suspend fun stopServer() {
        socketManager.stopServer()
    }

    override suspend fun syncClipboard(onReceived: (ClipBoardData) -> Unit) {
        socketManager.syncClipboard {
            onReceived(it)
        }
    }

    override suspend fun checkPrevSession() : Boolean {
        //check saved in sharedPref
        return if(sharedPref.isConnected()){
            //ping to socket
            val connectedDevice = sharedPref.getConnectedDevice()
            val isRunning = socketManager.ping(connectedDevice!!)
            println("check Server: $isRunning")
            if(isRunning){
                true
            }else{
                sharedPref.setConnectedDevice(null)
                false
            }
        }else{
            false
        }
    }
}