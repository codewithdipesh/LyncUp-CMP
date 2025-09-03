package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import com.codewithdipesh.lyncup.domain.model.Device

sealed class DeviceListAction {
    object StartDiscovery : DeviceListAction()
    data class ConnectToDevice(val device: Device) : DeviceListAction()
    data class DisconnectFromDevice(val device: Device) : DeviceListAction()
}
