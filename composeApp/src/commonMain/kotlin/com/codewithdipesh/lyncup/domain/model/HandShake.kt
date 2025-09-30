package com.codewithdipesh.lyncup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HandShake(
    val id : String = "",
    val name : String = "",
    val deviceType : DeviceType = DeviceType.ANDROID,
    val ip : String,
    val port : Int,
    val isConnected : Boolean = false,
    val lastSeen : Long,
)

fun HandShake.toDevice() : Device {
    return Device(
        id = id,
        name = name,
        deviceType = deviceType,
        ip = ip,
        port = port,
        isConnected = isConnected,
        lastSeen = lastSeen
    )
}