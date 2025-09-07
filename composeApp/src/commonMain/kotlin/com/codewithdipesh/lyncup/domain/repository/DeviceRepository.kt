package com.codewithdipesh.lyncup.domain.repository

import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.HandShake
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val deviceFlow : StateFlow<List<Device>>
    suspend fun startDiscovery()
    suspend fun stopDiscovery()
    suspend fun connectToDevice(device: Device) : Boolean
    suspend fun disconnectFromDevice(device: Device) : Boolean
    suspend fun startServer(
        onRequest: (HandShake, (Boolean) -> Unit) -> Unit,
        onClipboardReceived: (ClipBoardData) -> Unit
    ) : Boolean
    suspend fun stopServer()
    suspend fun syncClipboard(onReceived: (ClipBoardData) -> Unit)
}