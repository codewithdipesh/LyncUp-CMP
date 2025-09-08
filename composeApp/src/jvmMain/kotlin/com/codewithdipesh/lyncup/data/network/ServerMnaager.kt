package com.codewithdipesh.lyncup.data.network

import com.codewithdipesh.lyncup.domain.model.HandShake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.util.Collections
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class ServerManager {
    private var serverSocket : ServerSocket? = null
    private var serverJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val clients = Collections.synchronizedSet(mutableSetOf<Socket>())
    private var onMessageReceived: ((String) -> Unit)? = null
    private var onClientConnected: ((String) -> Unit)? = null
    private var onClientDisconnected: ((String) -> Unit)? = null

    fun startServer(
        port:Int = 8888,
        onMessage : (String) -> Unit = {},
        onRequest : (HandShake , (Boolean) -> Unit) -> Unit = {_,decide -> decide(true)},
        onConnect: (String) -> Unit = {},
        onDisconnect: (String) -> Unit = {}
    ){
        if(serverSocket != null ) return

        onMessageReceived = onMessage
        onClientConnected = onConnect
        onClientDisconnected = onDisconnect

        serverJob = scope.launch {
            try {
                serverSocket = ServerSocket(port)
                while(isActive && !serverSocket!!.isClosed){
                    val clientSocket = serverSocket!!.accept()
                    handleConnection(clientSocket, onRequest, onConnect)
                }
            } catch (e: Exception) {
                println("Server error: ${e.message}")
            }
        }
    }

    private suspend fun handleConnection(
        clientSocket: Socket,
        onRequest: (HandShake, (Boolean) -> Unit) -> Unit,
        onConnect: (String) -> Unit
    ) {
        try {
            clientSocket.soTimeout = 30000 // ✅ Longer timeout for user decision

            // Read handshake
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            val buf = CharArray(2048)
            val n = reader.read(buf)
            val firstChunk = if (n > 0) String(buf, 0, n) else ""

            val hello = if (firstChunk.startsWith("HELLO:")) {
                runCatching {
                    val json = firstChunk.removePrefix("HELLO:")
                    Json.decodeFromString<HandShake>(json)
                }.getOrNull()
            } else null

            if (hello == null) {
                clientSocket.getOutputStream().write("REJECTED\n".toByteArray())
                clientSocket.close()
                return
            }

            //proper waiting
            val decision = CompletableFuture<Boolean>()
            onRequest(hello) { approved ->
                decision.complete(approved)
            }

            //Wait for user decision
            val approved = withContext(Dispatchers.IO) {
                decision.get(30, TimeUnit.SECONDS) // 30 second timeout
            }

            if (!approved) {
                clientSocket.getOutputStream().write("REJECTED\n".toByteArray())
                clientSocket.close()
                return //Exit completely if rejected
            }

            //Only reach here if approved
            clientSocket.getOutputStream().write("ACCEPTED\n".toByteArray())
            clients.add(clientSocket)
            val clientAddress = clientSocket.inetAddress.hostAddress ?: "unknown"
            onConnect(clientAddress)

            // Now handle client messages
            handleClient(clientSocket, clientAddress)

        } catch (e: TimeoutException) {
            println("Connection timeout - no user response")
            clientSocket.close()
        } catch (e: Exception) {
            println("Connection error: ${e.message}")
            clientSocket.close()
        }
    }

    fun sendMessageToAll(message: String){
        synchronized(clients) {
            val data = message.toByteArray()
            clients.forEach { socket ->
                try {
                    socket.getOutputStream().write(data)
                }catch (e: Exception) {}
            }
        }
    }

    private fun handleClient(clientSocket: Socket, clientAddress: String) {
        try {
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            val buffer = CharArray(1024)

            while (!clientSocket.isClosed) {
                val bytesRead = reader.read(buffer)
                if (bytesRead == -1) break

                val message = String(buffer, 0, bytesRead)
                onMessageReceived?.invoke(message)
            }
        } catch (e: Exception) {
            println("Error handling client $clientAddress: ${e.message}")
        } finally {
            clients.remove(clientSocket)
            clientSocket.close()
            onClientDisconnected?.invoke(clientAddress)
        }
    }

    fun stopServer(){
        serverJob?.cancel()
        synchronized(clients) {
            clients.forEach { it.close() }
            clients.clear()
        }
        serverSocket?.close()
        serverJob = null
        serverSocket = null
    }

    fun isServerRunning() : Boolean = serverSocket != null && !serverSocket!!.isClosed



}