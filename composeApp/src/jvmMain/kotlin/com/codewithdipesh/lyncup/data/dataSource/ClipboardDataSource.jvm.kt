package com.codewithdipesh.lyncup.data.dataSource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

actual class ClipboardDataSource actual constructor(platformContext: Any) {

    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    private var monitorJob : Job? = null

    actual suspend fun getClipboard(): String? {
        return withContext(Dispatchers.IO) {
            try {
               val transferable = clipboard.getContents(null)
               if(transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                   transferable.getTransferData(DataFlavor.stringFlavor) as String
               } else {
                   null
               }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    actual suspend fun setClipboard(text: String): Boolean {
       return withContext(Dispatchers.IO) {
           try {
               val stringSelection = StringSelection(text)
               clipboard.setContents(stringSelection,null)
               true
           } catch (e: Exception) {
               false
           }
       }
    }

    actual fun startMonitoring(onChanged: (String) -> Unit) {
        if(monitorJob?.isActive == true) return
        monitorJob = CoroutineScope(Dispatchers.IO).launch {
            var lastText : String? = null
            while (isActive) {
                try {
                    val currentText = getClipboard()
                    if (currentText != null && currentText != lastText) {
                        lastText = currentText
                        onChanged(currentText)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(1000)
            }
        }
    }

    actual fun stopMonitoring() {
        monitorJob?.cancel()
        monitorJob = null
    }
}