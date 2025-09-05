package com.codewithdipesh.lyncup.data.network

import androidx.compose.ui.platform.Clipboard
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.HandShake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

actual class SocketManager actual constructor(){
    private val serverManager = ServerManager()

    //not for desktop
    actual suspend fun connectToDevice(device: Device): Boolean = false
    actual suspend fun syncClipboard(onSyncClipBoard: (ClipBoardData) -> Unit) {}


    actual suspend fun sendMessage(message: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                serverManager.sendMessageToAll(message)
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
        //Todo server is running but remove the device connection
       serverManager.stopServer()
    }

    actual suspend fun startServer(
        onRequest: (HandShake, (Boolean) -> Unit) -> Unit,
        onClipboardReceived: (ClipBoardData) -> Unit
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                serverManager.startServer(
                    port = 8888,
                    onRequest = { info, decide ->
                        onRequest(info,decide)
                    },
                    onMessage = {message ->
                        when {
                            message.startsWith("CLIPBOARD:") -> {
                                try {
                                    val clipboardData = Json.decodeFromString<ClipBoardData>(
                                        message.substringAfter("CLIPBOARD:")
                                    )
                                    onClipboardReceived(clipboardData)
                                } catch (e: Exception) {
                                    println("Failed to parse clipboard data: ${e.message}")
                                }
                            }
                            else -> {
                                println("Received message: $message")
                            }
                        }
                    }
                )
                true
            }catch (e: Exception) {
                false
            }
        }
    }

    actual suspend fun stopServer() {
        serverManager.stopServer()
    }

    actual fun isServerRunning(): Boolean = serverManager.isServerRunning()
}