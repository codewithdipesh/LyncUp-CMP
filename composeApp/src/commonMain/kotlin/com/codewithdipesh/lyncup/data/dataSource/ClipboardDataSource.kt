package com.codewithdipesh.lyncup.data.dataSource

import com.codewithdipesh.lyncup.domain.model.ClipBoardData

expect class ClipboardDataSource() {
    suspend fun getClipboard() : String?
    suspend fun setClipboard(text : String): Boolean
    fun startMonitoring(onChanged: (ClipBoardData) -> Unit)
    fun stopMonitoring()
}