package com.codewithdipesh.lyncup.data.service

import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

actual class LyncUpBackgroundService actual constructor(
    private val deviceRepository: DeviceRepository,
    private val clipboardRepository: ClipboardRepository
) {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    //it will be called when connection is successfully done
    //only start clipboard monitoring and send to other device
    //set clipboard received from other device
    actual fun startService() {
        serviceScope.launch {
            //received
            deviceRepository.syncClipboard { data->
                launch {
                    clipboardRepository.setClipboard(data.text)
                }
            }
            //send
            clipboardRepository.startClipboardMonitoring()
        }
    }
    actual fun stopService() {
        serviceScope.cancel()
        clipboardRepository.stopClipboardMonitoring()
    }
}