package com.codewithdipesh.lyncup.data.network

import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.domain.model.Device

expect class DeviceDiscoveryService {
    fun startDiscovery(onDevicesFound: (List<Device>) -> Unit)
    fun stopDiscovery()
}