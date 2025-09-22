package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import com.codewithdipesh.lyncup.domain.model.Device

sealed class DeviceListAction {
    object StartDiscovery : DeviceListAction()
    object StopDiscovery : DeviceListAction()
    data class ConnectToDevice(val device: Device) : DeviceListAction()
    data class DisconnectFromDevice(val device: Device) : DeviceListAction()
    object ApproveConnection : DeviceListAction()
    object RejectConnection : DeviceListAction()
    object GoToWifiSettings : DeviceListAction()
}
