package com.codewithdipesh.lyncup.data.dataSource

expect class ClipboardDataSource(platformContext: Any) {
    suspend fun getClipboard() : String?
    suspend fun setClipboard(text : String): Boolean
    fun startMonitoring(onChanged: (String) -> Unit)
    fun stopMonitoring()
}