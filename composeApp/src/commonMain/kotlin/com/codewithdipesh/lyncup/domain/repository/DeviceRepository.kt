package com.codewithdipesh.lyncup.domain.repository

import com.codewithdipesh.lyncup.domain.model.Device
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val deviceFlow : StateFlow<List<Device>>
    suspend fun startDiscovery()
    suspend fun stopDiscovery()
    suspend fun connectToDevice(device: Device) : Boolean
    suspend fun disconnectFromDevice(device: Device) : Boolean
}