package com.codewithdipesh.lyncup

import android.app.Application
import com.codewithdipesh.lyncup.di.androidModule
import com.codewithdipesh.lyncup.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class LyncUpApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.app = this
        initKoin {
            androidLogger()
            androidContext(this@LyncUpApp)
        }
    }
}