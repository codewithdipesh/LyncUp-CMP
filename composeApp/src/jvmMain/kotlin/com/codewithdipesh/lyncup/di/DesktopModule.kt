package com.codewithdipesh.lyncup.di

import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.ConnectionApprovalCoordinator
import org.koin.core.module.Module
import org.koin.dsl.module

val desktopModule = module {
    single { Unit }
    single { ConnectionApprovalCoordinator() }
}
actual fun platformModule() : Module = desktopModule