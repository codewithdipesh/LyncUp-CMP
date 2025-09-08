package com.codewithdipesh.lyncup.data.dataSource

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.codewithdipesh.lyncup.AppContextHolder
import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.domain.model.ClipBoardData
import com.codewithdipesh.lyncup.getPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

actual class ClipboardDataSource actual constructor() {

    private val context = AppContextHolder.app
    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private val sharedPref = SharedPreference
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

    actual fun startMonitoring(onChanged: (ClipBoardData) -> Unit) {
        listener = ClipboardManager.OnPrimaryClipChangedListener{
            CoroutineScope(Dispatchers.Main).launch {
                var lastText = getClipboard()
                while (isActive) {
                    try {
                        val currentText = getClipboard()
                        if (currentText != null && currentText != lastText) {
                            lastText = currentText
                            onChanged(
                                ClipBoardData(
                                    text = currentText,
                                    timeStamp = System.currentTimeMillis(),
                                    deviceId = sharedPref.getOrCreateDeviceId(),
                                    deviceName = getPlatform().name
                                )
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    delay(1000)
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