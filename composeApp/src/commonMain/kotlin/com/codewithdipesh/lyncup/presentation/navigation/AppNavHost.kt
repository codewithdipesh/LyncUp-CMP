package com.codewithdipesh.lyncup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.SessionCheckViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    platformType: PlatformType
) {
    val sessionVm: SessionCheckViewModel = koinViewModel()
    val isActive by sessionVm.isSessionActive.collectAsState()

    Navigator(Screen.DeviceListScreen(platformType)) { navigator ->
        LaunchedEffect(isActive) {
            if (isActive) navigator.replaceAll(Screen.SessionScreen)
            else navigator.replaceAll(
                Screen.DeviceListScreen(platformType)
            )
        }
        CurrentScreen()
    }
}