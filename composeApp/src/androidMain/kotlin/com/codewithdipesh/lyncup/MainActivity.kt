package com.codewithdipesh.lyncup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceListViewModel
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            DeviceConnectionScreen()
        }
    }
}

@Composable
fun DeviceConnectionScreen() {
    val viewModel: DeviceListViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    DeviceConnectionContent(
        state = state,
        onAction = viewModel::handleAction,
        platform = PlatformType.MOBILE
    )
}