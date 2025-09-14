package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.HandShake

data class DeviceListUI(
    val devices: List<Device> = emptyList(),
    val isDiscovering: Boolean = false,
    val connectedDevice: Device? = null,
    val error: String? = null,
    val pendingRequest: HandShake? = null, //only for desktops

    val deviceListShown : Boolean = false

)
