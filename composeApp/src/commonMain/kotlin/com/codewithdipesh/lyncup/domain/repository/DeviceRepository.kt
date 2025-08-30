package com.codewithdipesh.lyncup.domain.repository

import com.codewithdipesh.lyncup.domain.model.Device

interface DeviceRepository {
    suspend fun startDiscovery() : Result<Unit>
    suspend fun connectToDevice(deviceId: Device) : Result<Boolean>
    suspend fun disconnectFromDevice(deviceId: Device) : Result<Boolean>
}