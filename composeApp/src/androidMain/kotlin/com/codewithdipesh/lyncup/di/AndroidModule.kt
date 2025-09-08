package com.codewithdipesh.lyncup.di

import android.content.Context
import com.codewithdipesh.lyncup.data.dataSource.ClipboardDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val androidModule = module {

}

actual fun platformModule() : Module = androidModule