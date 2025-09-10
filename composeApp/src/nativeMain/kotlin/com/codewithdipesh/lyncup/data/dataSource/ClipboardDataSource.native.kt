package com.codewithdipesh.lyncup.data.dataSource

import com.codewithdipesh.lyncup.domain.model.ClipBoardData

actual class ClipboardDataSource actual constructor() {
    actual suspend fun getClipboard(): String? {
        TODO("Not yet implemented")
    }

    actual suspend fun setClipboard(text: String): Boolean {
        TODO("Not yet implemented")
    }

    actual fun startMonitoring(onChanged: (ClipBoardData) -> Unit) {
    }

    actual fun stopMonitoring() {
    }
}