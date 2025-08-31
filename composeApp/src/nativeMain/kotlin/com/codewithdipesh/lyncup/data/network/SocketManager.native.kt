package com.codewithdipesh.lyncup.data.network

import androidx.compose.ui.platform.Clipboard
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device

actual class SocketManager actual constructor() {
    actual suspend fun connectToDevice(device: Device): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun sendMessage(message: String): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun sendClipboard(clipboard: ClipBoardData): Boolean {
        TODO("Not yet implemented")
    }

    actual fun disconnect() {
    }
}