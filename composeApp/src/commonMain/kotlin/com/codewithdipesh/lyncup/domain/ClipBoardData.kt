package com.codewithdipesh.lyncup.domain

import kotlinx.serialization.Serializable

@Serializable
data class ClipBoardData(
    val text : String,
    val timeStamp : Long,
    val deviceId : String,
    val deviceName : String
)
