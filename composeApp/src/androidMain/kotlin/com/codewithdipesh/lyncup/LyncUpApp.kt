package com.codewithdipesh.lyncup

import android.app.Application

class LyncUpApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.app = this
    }
}