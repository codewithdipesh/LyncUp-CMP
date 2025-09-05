package com.codewithdipesh.lyncup.data.repository

import com.codewithdipesh.lyncup.data.dataSource.ClipboardDataSource
import com.codewithdipesh.lyncup.data.network.SocketManager
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClipboardRepositoryImpl(
    private val clipboardDataSource: ClipboardDataSource,
    private val socket : SocketManager
) : ClipboardRepository {

    private val _clipboardFlow = MutableStateFlow<ClipBoardData?>(null)
    override val clipboardFlow = _clipboardFlow.asStateFlow()

    override suspend fun getClipboardContent(): String? {
        val text = clipboardDataSource.getClipboard()
        return text
    }

    override suspend fun setClipboard(text: String): Boolean {
        val success = clipboardDataSource.setClipboard(text = text)
        return success
    }

    override fun startClipboardMonitoring() {
        clipboardDataSource.startMonitoring { newClipboard ->
            _clipboardFlow.value = newClipboard
            GlobalScope.launch {
                socket.sendClipboard(newClipboard)
            }
        }
    }

    override fun stopClipboardMonitoring() {
        clipboardDataSource.stopMonitoring()
    }
}