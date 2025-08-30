package com.codewithdipesh.lyncup.data.repository

import com.codewithdipesh.lyncup.data.dataSource.ClipboardDataSource
import com.codewithdipesh.lyncup.data.dataSource.NetworkDataSource
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClipboardRepositoryImpl(
    private val clipboardDataSource: ClipboardDataSource,
    private val networkDataSource: NetworkDataSource
) : ClipboardRepository {

    private val _clipboardFlow = MutableStateFlow<ClipBoardData?>(null)
    val clipboardFlow = _clipboardFlow.asStateFlow()

    override suspend fun getClipboardContent(): String? {
        val text = clipboardDataSource.getClipboard()
        return text
    }

    override suspend fun setClipboard(text: String): Boolean? {
        val success = clipboardDataSource.setClipboard(text = text)
        return success
    }

    override fun startClipboardMonitoring() {
        clipboardDataSource.startMonitoring { newClipboard ->
            _clipboardFlow.value = newClipboard
        }
    }

    override fun stopClipboardMonitoring() {
        clipboardDataSource.stopMonitoring()
    }

    override suspend fun syncClipboardToDeice(
        device: Device,
        text: String
    ): Boolean? {
        return try {
            val success = networkDataSource
        }
    }
}