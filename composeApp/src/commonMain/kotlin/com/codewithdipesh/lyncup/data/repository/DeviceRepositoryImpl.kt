package com.codewithdipesh.lyncup.data.repository

import com.codewithdipesh.lyncup.data.network.DeviceDiscoveryService
import com.codewithdipesh.lyncup.data.network.SocketManager
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DeviceRepositoryImpl(
    private val discoveryService : DeviceDiscoveryService,
    private val socketManager: SocketManager
): DeviceRepository {

    private val _deviceFlow = MutableStateFlow<List<Device>>(emptyList())
    override val deviceFlow = _deviceFlow.asStateFlow()

    private var connectedDevice: Device? = null

    override suspend fun startDiscovery() {
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
        _deviceFlow.value = emptyList()
    }

    override suspend fun connectToDevice(device: Device): Boolean {
        //prev device disconnection
        connectedDevice?.let { disconnectFromDevice(it) }

        val connected = socketManager.connectToDevice(device)
        if(connected) {
            connectedDevice = device
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
        }
        updateDeviceConnectionStatus(device,false)
        return true
    }

    override suspend fun syncClipboard(onReceived: (ClipBoardData) -> Unit) {
        socketManager.syncClipboard {
            onReceived(it)
        }
    }
}