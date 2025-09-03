package com.codewithdipesh.lyncup.data.network

import androidx.compose.ui.platform.Clipboard
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.Socket

actual class SocketManager actual constructor(){
    private var socket: Socket? = null

    actual suspend fun connectToDevice(device: Device): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket = Socket(device.ip, device.port)
                socket?.soTimeout = 10000
                true
            } catch (e: Exception){
                false
            }
        }
    }

    actual suspend fun sendMessage(message: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket?.getOutputStream()?.write(message.toByteArray())
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    actual suspend fun sendClipboard(clipboard: ClipBoardData): Boolean {
        val clipboardMessage = Json.encodeToString(clipboard)
        return sendMessage("CLIPBOARD:$clipboardMessage")
    }

    actual fun disconnect() {
        socket?.close()
        socket = null
    }
}