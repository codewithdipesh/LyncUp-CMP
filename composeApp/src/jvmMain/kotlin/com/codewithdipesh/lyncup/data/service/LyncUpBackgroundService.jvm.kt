package com.codewithdipesh.lyncup.data.service

import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.ConnectionApprovalCoordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class LyncUpService actual constructor(
    private val deviceRepository: DeviceRepository,
    private val clipboardRepository: ClipboardRepository
) : KoinComponent {
    private val connectionApproval : ConnectionApprovalCoordinator by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var isServerRunning = false

    //it will be called after app opens
    //start server and discovery
    //if connection is established then
    //start clipboard monitoring and send to other device
    //set clipboard received from other device

    actual fun startService() {
        if(isServerRunning){
            //if server running but i disconnect so start the discovery again
            serviceScope.launch {
                deviceRepository.startDiscovery()
                return@launch
            }
        }

        isServerRunning = true
        serviceScope.launch {
            println("Svc: starting server & discovery")
            deviceRepository.startServer(
                onRequest = { handshake, decide ->
                    serviceScope.launch {
                        println("Svc: onRequest from ${handshake}")
                        val approved = connectionApproval.onIncomingRequest(handshake)
                        println("Svc: decision=$approved")
                        if (approved) {
                            deviceRepository.stopDiscovery()
                            startMonitoring()
                        }
                        decide(approved)
                    }
                },
                onClipboardReceived = {
                    println("Svc: clipboard received")
                    serviceScope.launch { clipboardRepository.setClipboard(it.text) }
                },
                onError = {
                    println("Svc: connection error occurred")
                }
            )
            deviceRepository.startDiscovery()
        }
    }

    private fun startMonitoring(){
         serviceScope.launch {
            clipboardRepository.startClipboardMonitoring()
        }
    }

    actual fun stopService() {
        serviceScope.launch {
            deviceRepository.stopServer()
            deviceRepository.stopDiscovery()
            clipboardRepository.stopClipboardMonitoring()
        }
    }

    actual fun isServiceRunning(): Boolean {
        return isServerRunning
    }
}