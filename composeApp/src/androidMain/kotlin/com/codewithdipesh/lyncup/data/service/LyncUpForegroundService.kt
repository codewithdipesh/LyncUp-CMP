package com.codewithdipesh.lyncup.data.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.get

// In androidMain
class LyncUpForegroundService : Service() {
    private lateinit var lyncUpService: LyncUpBackgroundService
    
    override fun onCreate() {
        super.onCreate()
        lyncUpService = LyncUpBackgroundService(
            deviceRepository = get(),
            clipboardRepository = get()
        )
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        lyncUpService.startService()
        return START_STICKY
    }
    
    override fun onDestroy() {
        lyncUpService.stopService()
        super.onDestroy()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "lyncup_channel")
            .setContentTitle("LyncUp Running")
            .setContentText("Syncing...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }
}
