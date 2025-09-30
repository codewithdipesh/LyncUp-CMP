package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import android.content.Intent
import androidx.compose.ui.text.font.FontVariation.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.lyncup.AppContextHolder
import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.data.network.ConnectivityObserver
import com.codewithdipesh.lyncup.data.service.LyncUpBackgroundService
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

actual class DeviceViewModel actual constructor(
    private val deviceRepository: DeviceRepository,
    private val clipboardRepository: ClipboardRepository,
    private val backgroundService: LyncUpBackgroundService,
    private val connectivityObserver: ConnectivityObserver,
    private val sharedPreferences: SharedPreference
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceListUI())
    actual val state: StateFlow<DeviceListUI> = _state.asStateFlow()

    private var observingJob: Job? = null

    init {
        viewModelScope.launch {
            checkWifiMonitoring()
        }
    }

    actual suspend fun handleAction(action: DeviceListAction) {
        when(action){
            is DeviceListAction.ConnectToDevice -> connectToDevice(action.device)
            is DeviceListAction.DisconnectFromDevice -> disconnectFromDevice(action.device)
            DeviceListAction.StartDiscovery -> startDiscovery()
            DeviceListAction.StopDiscovery -> stopDiscovery()
            is DeviceListAction.ApproveConnection -> {} //no -op for mobile
            DeviceListAction.RejectConnection -> {}
            DeviceListAction.GoToWifiSettings -> goToWifiSettings()
        }
    }

    actual suspend fun startDiscovery() {
        viewModelScope.launch {
            //start observing
            observerDevices()
            _state.update {
                it.copy(
                    isDiscovering = true,
                    error = null
                )
            }
            try {
                deviceRepository.startDiscovery()
            } catch (e: Exception) {
                stopDiscovery()
                _state.value = _state.value.copy(
                    isDiscovering = false,
                    error = e.message ?: "Failed to start discovery"
                )
            }
        }
    }

    actual suspend fun stopDiscovery() {
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

    actual suspend fun connectToDevice(device: Device): Boolean {
        _state.update {
            it.copy(
                error = null,
                connectingRequest = device
            )
        }
        return try {
            //also save it if selected
            val connected = deviceRepository.connectToDevice(device)
            if(connected){
                _state.update {
                    it.copy(connectedDevice =  device,connectingRequest = null)
                }
                //connected successfully so now
                //stop discovery
                //start service
                stopDiscovery()

                backgroundService.startService()
            } else {
                _state.update {
                    it.copy(
                        error = "Failed to connect to ${device.name}",
                        connectingRequest = null
                    )
                }
            }
            connected
        } catch (e : Exception){
            _state.update {
                it.copy(error = e.message ?: "Failed to connect to ${device.name}",connectingRequest = null)
            }
            false
        }
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
        backgroundService.stopService()
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

    actual fun checkWifiMonitoring() {
        if(observingJob!= null && observingJob?.isActive == true) return
        //starting observing
        connectivityObserver.startObserving()

        observingJob = viewModelScope.launch {
            connectivityObserver.isConnected.collect { isConnected ->
                _state.update { it.copy(isWifiAvailable = isConnected) }
            }
        }
    }


    actual fun stopWifiMonitoring() {
        observingJob?.cancel()
        observingJob = null
        connectivityObserver.stopObserving()
    }

    actual override fun onCleared() {
        super.onCleared()
        stopWifiMonitoring()
    }

    actual fun goToWifiSettings() {
        val context = AppContextHolder.context
        val intent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
