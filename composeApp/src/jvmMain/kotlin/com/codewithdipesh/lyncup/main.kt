package com.codewithdipesh.lyncup

import androidx.compose.foundation.clickable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import com.codewithdipesh.lyncup.presentation.ui.LyncUpTheme
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "LyncUp",
    ) {
        KoinContext {
            LyncUpTheme {
                DeviceConnectionScreen()
            }
        }
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

    state.pendingRequest?.let { req ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { /* block dismiss */ },
            title = { androidx.compose.material3.Text("Connection request") },
            text = { androidx.compose.material3.Text("${req.name} wants to connect") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = { scope.launch {
                        viewModel.handleAction(DeviceListAction.ApproveConnection)
                    } }
                ) { androidx.compose.material3.Text("Approve") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { scope.launch { viewModel.handleAction(DeviceListAction.RejectConnection) } }
                ) { androidx.compose.material3.Text("Reject") }
            }
        )
    }
}