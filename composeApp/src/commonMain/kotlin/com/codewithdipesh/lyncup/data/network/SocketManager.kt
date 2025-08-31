package com.codewithdipesh.lyncup.data.network

import androidx.compose.ui.platform.Clipboard
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device

expect class SocketManager() {
    suspend fun connectToDevice(device: Device): Boolean
    suspend fun sendMessage(message: String): Boolean
    suspend fun sendClipboard(clipboard: ClipBoardData): Boolean
    fun disconnect()
}