package com.codewithdipesh.lyncup.domain.model

import kotlinx.serialization.Serializable

data class Device(
    val id : String,
    val name : String,
    val ip : String,
    val port : Int,
    val deviceType : DeviceType,
    val isConnected : Boolean = false,
    val lastSeen : Long,
)


@Serializable
enum class DeviceType { ANDROID, DESKTOP, IOS }