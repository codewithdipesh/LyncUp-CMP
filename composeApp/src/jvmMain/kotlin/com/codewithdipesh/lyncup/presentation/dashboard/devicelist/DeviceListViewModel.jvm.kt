package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.data.network.ConnectivityObserver
import com.codewithdipesh.lyncup.data.service.LyncUpBackgroundService
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

actual class DeviceViewModel actual constructor(
    private val deviceRepository: DeviceRepository,
    private val clipboardRepository: ClipboardRepository,
    private val backgroundService: LyncUpBackgroundService,
    private val connectivityObserver: ConnectivityObserver,
    private val sharedPreferences: SharedPreference
) : ViewModel() {

    private val connectionApproval: ConnectionApprovalCoordinator by inject(ConnectionApprovalCoordinator::class.java)
    private val _state = MutableStateFlow(DeviceListUI())
    actual val state: StateFlow<DeviceListUI> = _state.asStateFlow()

    init {
        _state.update {
            it.copy(isDiscovering = true)
        }
        viewModelScope.launch {
            //wifi monitoring
            backgroundService.startService()
            observerDevices()
            connectionApproval.requests.collect {request ->
               if(request != null){
                   println("Svc: viewmodel request received onRequest from ${request}")
                   _state.update {
                       it.copy(pendingRequest = request)
                   }
               }
            }
        }
    }

    actual suspend fun handleAction(action: DeviceListAction) {
        when(action){
            is DeviceListAction.ConnectToDevice -> {}//not for desktop
            is DeviceListAction.DisconnectFromDevice -> disconnectFromDevice(action.device)
            DeviceListAction.StartDiscovery -> startDiscovery()
            is DeviceListAction.ApproveConnection -> {
                connectionApproval.approve()
                _state.update {
                    it.copy(
                        pendingRequest = null,
                        isDiscovering = false
                    )
                }
                //sharedPref Update
                sharedPreferences.setConnectedDevice(action.device)

            }
            DeviceListAction.RejectConnection -> {
                connectionApproval.reject()
                _state.update {
                    it.copy(pendingRequest = null)
                }
            }

            DeviceListAction.StopDiscovery -> {}
            DeviceListAction.GoToWifiSettings -> {}
        }
    }

    actual suspend fun startDiscovery() {
        //no -op for jvm its already started in service
        //if forcefully want to restart
        backgroundService.startService()
    }

    actual suspend fun stopDiscovery() {
        //same as startDiscovery
        backgroundService.stopService()
    }

    actual suspend fun connectToDevice(device: Device): Boolean {
        //no -op for jvm
        return false
    }

    actual suspend fun disconnectFromDevice(device: Device) {
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
        //for jvm if disconnected start discovery and other things again
        backgroundService.startService()
        _state.update {
            it.copy(isDiscovering = true)
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
    actual override fun onCleared() {
        super.onCleared()
        stopWifiMonitoring()
        viewModelScope.launch {
            deviceRepository.stopDiscovery()
        }
    }

    actual fun checkWifiMonitoring() {
        //no -op for jvm desktops
    }

    actual fun stopWifiMonitoring() {
        connectivityObserver.stopObserving()
    }

    actual fun goToWifiSettings() {
        //no -op
    }
}