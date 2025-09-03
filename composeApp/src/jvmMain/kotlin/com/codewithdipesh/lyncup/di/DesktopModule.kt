package com.codewithdipesh.lyncup.di

import org.koin.core.module.Module
import org.koin.dsl.module

val desktopModule = module {
    single { Unit }
}
actual fun platformModule() : Module = desktopModule