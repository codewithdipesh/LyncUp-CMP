package com.codewithdipesh.lyncup.data.network

import com.codewithdipesh.lyncup.data.dataStore.SharedPreference

actual class DeviceDiscoveryService actual constructor(sharedPref: SharedPreference) {
    actual fun startDiscovery(onDevicesFound: (List<String>) -> Unit) {
    }

    actual fun stopDiscovery() {
    }
}