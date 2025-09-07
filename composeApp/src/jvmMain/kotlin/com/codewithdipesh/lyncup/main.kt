package com.codewithdipesh.lyncup

import androidx.compose.foundation.clickable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.codewithdipesh.lyncup.di.initKoin
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceListAction
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceViewModel
import kotlinx.coroutines.launch
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
    val viewModel: DeviceViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    DeviceConnectionContent(
        state = state,
        onAction = {
            scope.launch {
                viewModel.handleAction(it)
            }
        },
        platform = PlatformType.DESKTOP
    )
    if(state.pendingRequest != null){
        AlertDialog(
            onDismissRequest = {
                scope.launch {
                    viewModel.handleAction(DeviceListAction.RejectConnection)
                }
            },
            title = {
                Text("Connection Request")
            },
            text = {
                Text("You have a new connection request from ${state.pendingRequest!!.name}")
            },
            confirmButton = {
                Text("Accept",
                    modifier = Modifier.clickable {
                        scope.launch {
                            viewModel.handleAction(DeviceListAction.ApproveConnection)
                        }
                    }
                )
            },
            dismissButton = {
                Text("Reject",
                    modifier = Modifier.clickable {
                        scope.launch {
                            viewModel.handleAction(DeviceListAction.RejectConnection)
                        }
                    }
                )
            }
        )
    }
}