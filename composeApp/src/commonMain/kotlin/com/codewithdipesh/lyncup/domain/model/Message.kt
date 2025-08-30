package com.codewithdipesh.lyncup.domain.model

data class Message(
    val id : String,
    val name : String,
    val ip : String,
    val port : Int,
    val deviceType : DeviceType,
    val isConnected : Boolean = false,
    val lastSeen : Long,
)