package com.codewithdipesh.lyncup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceViewModel
import com.codewithdipesh.lyncup.presentation.dashboard.session.SessionScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


sealed class Screen{
    data class DeviceListScreen(val platform: PlatformType) : Screen {
        @Composable
        override fun Content() {
            val deviceViewModel: DeviceViewModel = koinViewModel()
            val state = deviceViewModel.state.collectAsState().value
            val scope = rememberCoroutineScope()
            val navigator = LocalNavigator.currentOrThrow

            LaunchedEffect(state.connectedDevice?.id) {
                if (state.connectedDevice != null) {
                    println("NAV: connected -> replaceAll(SessionScreen)")
                    navigator.replaceAll(SessionScreen)
                }
            }

            DeviceConnectionContent(
                state = state,
                onAction = { action -> scope.launch { deviceViewModel.handleAction(action) } },
                platform = platform,
                navigator = navigator
            )
        }
    }

    object SessionScreen : Screen {
        @Composable
        override fun Content() {
            SessionScreen()
        }
    }
}