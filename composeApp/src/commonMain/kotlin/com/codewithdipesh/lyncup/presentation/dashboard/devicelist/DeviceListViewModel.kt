package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import androidx.lifecycle.ViewModel
import com.codewithdipesh.lyncup.data.network.ConnectivityObserver
import com.codewithdipesh.lyncup.data.service.LyncUpBackgroundService
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.StateFlow

expect class DeviceViewModel(
    deviceRepository: DeviceRepository,
    clipboardRepository: ClipboardRepository,
    backgroundService: LyncUpBackgroundService,
    connectivityObserver: ConnectivityObserver
) : ViewModel {
    val state: StateFlow<DeviceListUI>

    suspend fun handleAction(action : DeviceListAction)
    suspend fun startDiscovery()
    suspend fun stopDiscovery()
    suspend fun connectToDevice(device: Device): Boolean
    suspend fun disconnectFromDevice(device: Device)
    fun checkWifiMonitoring()
    fun stopWifiMonitoring()
    fun goToWifiSettings()

    override fun onCleared()
}