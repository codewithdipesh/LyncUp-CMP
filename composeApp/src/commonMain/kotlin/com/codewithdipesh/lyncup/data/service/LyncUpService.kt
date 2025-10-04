package com.codewithdipesh.lyncup.data.service

import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository

expect class LyncUpService(
    deviceRepository: DeviceRepository,
    clipboardRepository: ClipboardRepository
) {
    fun startService()
    fun stopService()

    fun isServiceRunning(): Boolean
}