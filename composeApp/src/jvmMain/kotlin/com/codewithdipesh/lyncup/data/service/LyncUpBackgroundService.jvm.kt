package com.codewithdipesh.lyncup.data.service

import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.ConnectionApprovalCoordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

actual class LyncUpBackgroundService actual constructor(
    private val deviceRepository: DeviceRepository,
    private val clipboardRepository: ClipboardRepository
) {
    private val connectionApproval : ConnectionApprovalCoordinator by inject(ConnectionApprovalCoordinator::class.java)
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var isServerRunning = false

    //it will be called after app opens
    //start server and discovery
    //if connection is established then
    //start clipboard monitoring and send to other device
    //set clipboard received from other device

    actual fun startService() {
        if(isServerRunning) return

        isServerRunning = true
        serviceScope.launch {
            deviceRepository.startServer(
                onRequest = {handshake,decide->
                    launch {
                        val approved = connectionApproval.onIncomingRequest(handshake)
                        if(approved) {
                            //sending
                            startMonitoring()
                        }
                        decide(approved)
                    }
                },
                onClipboardReceived = {
                    //receiving
                    launch {
                        clipboardRepository.setClipboard(it.text)
                    }
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
}