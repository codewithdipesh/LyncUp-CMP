package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import com.codewithdipesh.lyncup.domain.model.Device

data class DeviceListUI(
    val devices: List<Device> = emptyList(),
    val isDiscovering: Boolean = false,
    val connectedDevice: Device? = null,
    val error: String? = null
)
