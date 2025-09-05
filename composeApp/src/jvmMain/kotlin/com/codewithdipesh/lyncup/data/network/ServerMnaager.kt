package com.codewithdipesh.lyncup.data.network

import com.codewithdipesh.lyncup.domain.model.HandShake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.util.Collections
import java.util.concurrent.CompletableFuture

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
                    clientSocket.soTimeout = 5000

                    //first chunk as handshake
                    val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                    val buf = CharArray(2048)
                    val n = reader.read(buf)
                    val firstChunk = if (n > 0) String(buf, 0, n) else ""

                    lateinit var hello : HandShake
                    if (firstChunk.startsWith("HELLO:")) {
                        runCatching {
                            val json = firstChunk.removePrefix("HELLO:")
                            hello = Json.decodeFromString(HandShake.serializer(), json)
                        }
                    }
                    //ask UI
                    val decision = CompletableFuture<Boolean>()
                    onRequest(
                        HandShake(hello.id,hello.name,hello.deviceType)
                    ){approved ->
                        decision.complete(approved)
                    }

                    //rejected
                    if(!decision.get()){
                        clientSocket.getOutputStream().write("REJECTED".toByteArray())
                        clientSocket.close()
                        continue
                    }
                    //accepted
                    clientSocket.getOutputStream().write("ACCEPTED".toByteArray())
                    clients.add(clientSocket)
                    val clientAddress = clientSocket.inetAddress.hostAddress
                    onClientConnected?.invoke(clientAddress)

                    //handle client in separate coroutine
                    launch {
                        handleClient(clientSocket, clientAddress)
                    }
                }
            } catch (e: Exception) {
                // Handle exception
            }
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