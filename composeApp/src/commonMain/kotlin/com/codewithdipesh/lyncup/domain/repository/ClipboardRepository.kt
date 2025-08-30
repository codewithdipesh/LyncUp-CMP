package com.codewithdipesh.lyncup.domain.repository

import com.codewithdipesh.lyncup.domain.model.Device

interface ClipboardRepository {
    suspend fun getClipboardContent(): String?
    suspend fun setClipboard(text : String): Boolean?
    fun startClipboardMonitoring()
    fun stopClipboardMonitoring()
    suspend fun syncClipboardToDeice(device: Device, text: String): Boolean?
}