package com.codewithdipesh.lyncup.presentation.dashboard.session

import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device

data class SessionUI(
    val connectedDevice : Device,
    val sessionTimeinMinutes : Int,
    val battery : Int,
    val isConnected : Boolean,
    val sharedClipboards : List<ClipBoardData>
)
