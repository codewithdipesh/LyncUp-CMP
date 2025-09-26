package com.codewithdipesh.lyncup

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.HomeScreen
import com.codewithdipesh.lyncup.presentation.dashboard.SessionCheckViewModel
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceViewModel
import com.codewithdipesh.lyncup.presentation.ui.LyncUpTheme
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(1))
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                LyncUpTheme {
                    val authViewModel : SessionCheckViewModel = koinViewModel()
                    val deviceViewModel: DeviceViewModel = koinViewModel()
                    HomeScreen(
                        authViewModel = authViewModel,
                        deviceViewModel = deviceViewModel,
                        platformType = PlatformType.MOBILE
                    )
                }
            }
        }
    }
}