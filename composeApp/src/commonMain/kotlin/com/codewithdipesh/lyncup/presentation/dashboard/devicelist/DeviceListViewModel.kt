package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeviceListViewModel(
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DeviceListUI())
    val state = _state.asStateFlow()

    fun handleAction ( action : DeviceListAction){
        when(action){
            is DeviceListAction.ConnectToDevice -> connectToDevice(action.device)
            is DeviceListAction.DisconnectFromDevice -> disconnectFromDevice(action.device)
            DeviceListAction.StartDiscovery -> startDiscovery()
        }
    }

    init {
        observerDevices()
    }

    fun startDiscovery(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isDiscovering = true,
                    error = null
                )
            }
            try {
                deviceRepository.startDiscovery()
                delay(30000)
                stopDiscovery()
            } catch (e: Exception) {
                stopDiscovery()
                _state.value = _state.value.copy(
                    isDiscovering = false,
                    error = e.message ?: "Failed to start discovery"
                )
            }
        }
    }

    fun stopDiscovery(){
        viewModelScope.launch {
            deviceRepository.stopDiscovery()
            _state.update {
                it.copy(
                    isDiscovering = false,
                    error = null
                )
            }
        }
    }

    fun connectToDevice(device : Device){
        viewModelScope.launch {
            _state.update {
                it.copy(error = null)
            }
            try {
                val connected = deviceRepository.connectToDevice(device)
                if(connected){
                    _state.update {
                        it.copy(connectedDevice =  device)
                    }
                    stopDiscovery()
                } else {
                    _state.update {
                        it.copy(error = "Failed to connect to ${device.name}")
                    }
                }
            } catch (e : Exception){
                _state.update {
                    it.copy(error = e.message ?: "Failed to connect to ${device.name}")
                }
            }
        }
    }

    fun disconnectFromDevice(device: Device) {
        viewModelScope.launch {
            try {
                val disconnected = deviceRepository.disconnectFromDevice(device)
                if (disconnected && _state.value.connectedDevice?.id == device.id) {
                    _state.update {
                        it.copy(connectedDevice = null)
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message)
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun observerDevices(){
        viewModelScope.launch {
            deviceRepository.deviceFlow.collect { devices ->
                _state.update {
                    it.copy(devices = devices)
                }
                val currentConnectedDevice = devices.find { it.isConnected }
                _state.update{
                    it.copy(connectedDevice = currentConnectedDevice)
                }
                if (currentConnectedDevice != null && _state.value.isDiscovering) {
                    stopDiscovery()
                }
            }

        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            deviceRepository.stopDiscovery()
        }
    }

}