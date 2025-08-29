package com.codewithdipesh.lyncup.data.dataSource

actual class ClipboardDataSource actual constructor(platformContext: Any) {
    actual suspend fun getClipboard(): String? {
        TODO("Not yet implemented")
    }

    actual suspend fun setClipboard(text: String): Boolean {
        TODO("Not yet implemented")
    }

    actual fun startMonitoring(onChanged: (String) -> Unit) {
    }

    actual fun stopMonitoring() {
    }
}