package com.codewithdipesh.lyncup.data.network

import androidx.compose.ui.platform.Clipboard
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.HandShake

expect class SocketManager() {
    //mobile functionality
    suspend fun connectToDevice(device: Device): Boolean
    suspend fun syncClipboard(onSyncClipBoard : (ClipBoardData) -> Unit )

    //Server functionality
    suspend fun startServer(
        onRequest: (HandShake, (Boolean) -> Unit) -> Unit,
        onClipboardReceived: (ClipBoardData) -> Unit
    ): Boolean
    suspend fun stopServer()
    fun isServerRunning(): Boolean

    //common
    suspend fun sendMessage(message: String): Boolean
    suspend fun sendClipboard(clipboard: ClipBoardData): Boolean
    fun disconnect()
}