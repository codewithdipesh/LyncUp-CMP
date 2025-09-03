package com.codewithdipesh.lyncup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.codewithdipesh.lyncup.di.initKoin
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceListViewModel
import org.koin.compose.viewmodel.koinViewModel

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "LyncUp",
    ) {
        DeviceConnectionScreen()
    }
}

@Composable
fun DeviceConnectionScreen() {
    val viewModel: DeviceListViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    DeviceConnectionContent(
        state = state,
        onAction = viewModel::handleAction,
        platform = PlatformType.DESKTOP
    )
}