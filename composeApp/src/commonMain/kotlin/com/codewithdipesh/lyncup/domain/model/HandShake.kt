package com.codewithdipesh.lyncup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HandShake(
    val id : String = "",
    val name : String = "",
    val deviceType : DeviceType = DeviceType.ANDROID
)