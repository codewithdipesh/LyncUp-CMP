package com.codewithdipesh.lyncup.data.dataSource

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual class ClipboardDataSource actual constructor(platformContext: Any) {

    private val context = platformContext as Context
    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private var listener : ClipboardManager.OnPrimaryClipChangedListener? = null

    actual suspend fun getClipboard(): String? {
        return try {
            val clip = clipboardManager.primaryClip
            clip?.getItemAt(0)?.text?.toString()
        } catch (e: Exception) {
            null
        }
    }

    actual suspend fun setClipboard(text: String): Boolean {
        return try {
            val clip = ClipData.newPlainText("text",text)
            clipboardManager.setPrimaryClip(clip)
            true
        }catch (e: Exception) {
            false
        }
    }

    actual fun startMonitoring(onChanged: (String) -> Unit) {
        listener = ClipboardManager.OnPrimaryClipChangedListener{
            CoroutineScope(Dispatchers.Main).launch {
                val text = getClipboard()
                if (text != null) {
                    onChanged(text)
                }
            }
        }
        clipboardManager.addPrimaryClipChangedListener(listener)
    }

    actual fun stopMonitoring() {
        listener?.let { clipboardManager.removePrimaryClipChangedListener(it)}
        listener = null
    }
}