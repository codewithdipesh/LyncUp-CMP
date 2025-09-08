package com.codewithdipesh.lyncup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceViewModel
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            KoinContext {
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
        platform = PlatformType.MOBILE
    )
}