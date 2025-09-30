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
            clientSocket.soTimeout = 30000  // 30 second read timeout
            clientSocket.keepAlive = true   // Enable TCP keep-alive
            clientSocket.tcpNoDelay = true

            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            val firstLine = reader.readLine()?.trim() ?: ""
            println("Svc: $firstLine")

            val hello = if (firstLine.startsWith("HELLO:")) {
                runCatching {
                    val json = firstLine.removePrefix("HELLO:")
                    Json.decodeFromString<HandShake>(json)
                }.getOrNull()
            } else null

            if (hello == null) {
                clientSocket.getOutputStream().write("REJECTED\n".toByteArray())
                clientSocket.close()
                return
            }

            val decision = CompletableFuture<Boolean>()
            onRequest(hello) { approved -> decision.complete(approved) }

            val approved = withContext(Dispatchers.IO) {
                decision.get(30, TimeUnit.SECONDS)
            }

            if (!approved) {
                clientSocket.getOutputStream().write("REJECTED\n".toByteArray())
                clientSocket.close()
                return
            }

            clientSocket.getOutputStream().write("ACCEPTED\n".toByteArray())
            clients.add(clientSocket)
            val clientAddress = clientSocket.inetAddress.hostAddress ?: "unknown"
            onConnect(clientAddress)
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
            val data = (if (message.endsWith("\n")) message else "$message\n").toByteArray()
            clients.forEach { socket ->
                try {
                    socket.getOutputStream().write(data)
                }catch (e: Exception) {}
            }
        }
    }

    private fun handleClient(clientSocket: Socket, clientAddress: String) {
        try {
            clientSocket.soTimeout = 30000
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

            while (!clientSocket.isClosed) {
                val message = reader.readLine() // This will now timeout after 30 seconds
                if (message == null) {
                    println("Client disconnected normally: $clientAddress")
                    break
                }
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