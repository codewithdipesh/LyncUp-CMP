package com.codewithdipesh.lyncup

import androidx.compose.foundation.clickable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithdipesh.lyncup.di.initKoin
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.HomeScreen
import com.codewithdipesh.lyncup.presentation.dashboard.SessionCheckViewModel
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceListAction
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceViewModel
import com.codewithdipesh.lyncup.presentation.ui.LyncUpTheme
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "LyncUp Desktop",
        state = remember { WindowState(width = 1000.dp, height = 600.dp) }
    ) {
        val window = window as ComposeWindow
        LaunchedEffect(Unit) {
            window.minimumSize = java.awt.Dimension(600, 500)
        }
        KoinContext {
            LyncUpTheme {
                val authViewModel : SessionCheckViewModel = koinViewModel()
                val deviceViewModel: DeviceViewModel = koinViewModel()
                val state by deviceViewModel.state.collectAsState()
                val scope = rememberCoroutineScope()

                HomeScreen(
                    authViewModel = authViewModel,
                    deviceViewModel = deviceViewModel,
                    platformType = PlatformType.DESKTOP
                )

                state.pendingRequest?.let { req ->
                    AlertDialog(
                        onDismissRequest = { /* block dismiss */ },
                        title = { Text("Connection request") },
                        text = { Text("${req.name} wants to connect") },
                        confirmButton = {
                            TextButton(
                                onClick = { scope.launch {
                                    deviceViewModel.handleAction(DeviceListAction.ApproveConnection)
                                } }
                            ) { Text("Approve") }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { scope.launch { deviceViewModel.handleAction(DeviceListAction.RejectConnection) } }
                            ) { Text("Reject") }
                        }
                    )
                }
            }
        }
    }
}