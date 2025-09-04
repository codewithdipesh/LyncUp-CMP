package com.codewithdipesh.lyncup.data.network

import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.DeviceType
import com.codewithdipesh.lyncup.domain.model.HandShake
import com.codewithdipesh.lyncup.getPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.Socket

actual class SocketManager actual constructor() {
    private var socket: Socket? = null

    actual suspend fun connectToDevice(device: Device): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket = Socket(device.ip, device.port)
                socket?.soTimeout = 10000

                //SEND handshake
                val hello = HandShake(
                    id = SharedPreference.getOrCreateDeviceId(),
                    name = getPlatform().name,
                    deviceType = DeviceType.ANDROID
                )
                val json = Json.encodeToString(hello)
                socket?.getOutputStream()?.write("HELLO:$json".toByteArray())
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

    //not needed for mobile
    actual suspend fun startServer(): Boolean = false
    actual suspend fun stopServer() {}
    actual fun isServerRunning(): Boolean = false
}