package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import androidx.lifecycle.ViewModel
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.StateFlow

actual class DeviceViewModel actual constructor(deviceRepository: DeviceRepository) : ViewModel() {
    actual val state: StateFlow<DeviceListUI>
        get() = TODO("Not yet implemented")
    actual val isServerRunning: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    actual suspend fun startDiscovery() {
    }

    actual suspend fun stopDiscovery() {
    }

    actual suspend fun connectToDevice(device: Device): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun disconnectFromDevice(device: Device) {
        TODO("Not yet implemented")
    }

    actual suspend fun syncClipboard() {
    }

    actual suspend fun sendClipboard(clipboard: ClipBoardData): Boolean {
        TODO("Not yet implemented")
    }
}