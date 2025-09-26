package com.codewithdipesh.lyncup.presentation.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceConnectionContent
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    authViewModel: SessionCheckViewModel,
    deviceViewModel: DeviceViewModel,
    platformType: PlatformType
){
    val authState by authViewModel.isSessionActive.collectAsState()
    val deviceState by deviceViewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    if(authState){
        //redirect to session screen
        println("Redirecting to Session Screen")
        Text("Session Screen")
    }else{
        DeviceConnectionContent(
            state = deviceState,
            onAction = {
                scope.launch {
                    deviceViewModel.handleAction(it)
                }
            },
            platform = platformType
        )
    }

}