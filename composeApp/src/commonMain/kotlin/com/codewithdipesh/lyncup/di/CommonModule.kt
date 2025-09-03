package com.codewithdipesh.lyncup.di

import com.codewithdipesh.lyncup.data.dataSource.ClipboardDataSource
import com.codewithdipesh.lyncup.data.dataStore.SettingsProvider
import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.data.network.DeviceDiscoveryService
import com.codewithdipesh.lyncup.data.network.SocketManager
import com.codewithdipesh.lyncup.data.repository.ClipboardRepositoryImpl
import com.codewithdipesh.lyncup.data.repository.DeviceRepositoryImpl
import com.codewithdipesh.lyncup.domain.repository.ClipboardRepository
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    //Data sources
    single{ ClipboardDataSource(get()) }
    single { SettingsProvider }
    single { SharedPreference }

    //services
    single { DeviceDiscoveryService() }
    single { SocketManager() }

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
            socketManager = get()
        )
    }

    //viewModels
    viewModel {
        DeviceListViewModel(
            deviceRepository = get()
        )
    }
}