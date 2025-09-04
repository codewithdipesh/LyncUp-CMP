package com.codewithdipesh.lyncup.data.network

import androidx.compose.ui.platform.Clipboard
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device

expect class SocketManager() {
    //mobile functionality
    suspend fun connectToDevice(device: Device): Boolean

    //Server functionality
    suspend fun startServer(): Boolean
    suspend fun stopServer()
    fun isServerRunning(): Boolean

    //common
    suspend fun sendMessage(message: String): Boolean
    suspend fun sendClipboard(clipboard: ClipBoardData): Boolean
    fun disconnect()
}