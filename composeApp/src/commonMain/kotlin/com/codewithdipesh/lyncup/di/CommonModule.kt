package com.codewithdipesh.lyncup.di

import com.codewithdipesh.lyncup.data.dataSource.ClipboardDataSource
import com.codewithdipesh.lyncup.data.dataStore.SettingsProvider
import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.data.network.ConnectivityObserver
import com.codewithdipesh.lyncup.data.network.DeviceDiscoveryService
import com.codewithdipesh.lyncup.data.network.SocketManager
import com.codewithdipesh.lyncup.data.repository.ClipboardRepositoryImpl
import com.codewithdipesh.lyncup.data.repository.DeviceRepositoryImpl
import com.codewithdipesh.lyncup.data.service.LyncUpBackgroundService
import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import com.codewithdipesh.lyncup.presentation.dashboard.SessionCheckViewModel
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    //Data sources
    single { SettingsProvider }
    single { SharedPreference }
    single { ClipboardDataSource() }

    //services
    single { DeviceDiscoveryService() }
    single { SocketManager() }
    single { ConnectivityObserver() }


    //repository
    single<ClipboardRepository> {
        ClipboardRepositoryImpl(
            clipboardDataSource = get(),
            socket = get()
        )
    }
    single<DeviceRepository> {
        DeviceRepositoryImpl(
            discoveryService = get(),
            socketManager = get(),
            sharedPref = get()
        )
    }

    //background service
    single {
        LyncUpBackgroundService(
        deviceRepository = get(),
        clipboardRepository = get()
    )}

    //viewModels
    viewModel {
        DeviceViewModel(
            deviceRepository = get(),
            clipboardRepository = get(),
            backgroundService = get(),
            connectivityObserver = get(),
            sharedPreferences = get()
        )
    }
    viewModel {
        SessionCheckViewModel(
            repo = get()
        )
    }
}