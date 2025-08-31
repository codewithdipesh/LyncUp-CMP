package com.codewithdipesh.lyncup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceAnnouncement(
    val deviceId: String,
    val deviceName: String,
    val port: Int,
    val deviceType: DeviceType
)