package com.codewithdipesh.lyncup.data.service

import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository

actual class LyncUpBackgroundService actual constructor(
    deviceRepository: DeviceRepository,
    clipboardRepository: ClipboardRepository
) {
    actual fun startService() {
    }

    actual fun stopService() {
    }
}