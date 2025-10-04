package com.codewithdipesh.lyncup

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.codewithdipesh.lyncup.di.androidModule
import com.codewithdipesh.lyncup.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class LyncUpApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.app = this
        createNotificationChannels()
        initKoin {
            androidLogger()
            androidContext(this@LyncUpApp)
        }
    }


    private fun createNotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "lyncup_channel",
                "LyncUp Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows LyncUp sync status"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}