package com.codewithdipesh.lyncup

import android.app.Application
import android.content.Context

object AppContextHolder {
    lateinit var app: Application
    val context: Context
        get() = app.applicationContext
}