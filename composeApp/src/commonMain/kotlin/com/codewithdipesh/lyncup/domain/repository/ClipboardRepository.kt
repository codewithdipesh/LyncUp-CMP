package com.codewithdipesh.lyncup.domain.repository

import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import kotlinx.coroutines.flow.StateFlow

interface ClipboardRepository {
    val clipboardFlow: StateFlow<ClipBoardData?>
    suspend fun getClipboardContent(): String?
    suspend fun setClipboard(text : String): Boolean
    fun startClipboardMonitoring()
    fun stopClipboardMonitoring()
    suspend fun syncClipboardToDeice(clipboard: ClipBoardData): Boolean
}