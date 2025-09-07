package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codewithdipesh.lyncup.domain.model.PlatformType

@Composable
fun DeviceConnectionContent(
    state : DeviceListUI,
    onAction : (DeviceListAction) -> Unit,
    platform: PlatformType
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        if(state.connectedDevice == null){
            state.devices?.let {
                it.forEach { device ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(Color.LightGray)
                            .clickable {
                                if(!state.isDiscovering){
                                    onAction(DeviceListAction.ConnectToDevice(device))
                                }
                            }
                    ){
                        Text(device.name)
                    }
                }
            }
        }else{
            Text("Connected to ${state.connectedDevice.name}")
        }
        if (platform == PlatformType.MOBILE){
            Box(
                modifier = Modifier.size(
                    width = 200.dp,
                    height = 50.dp
                )
                    .padding(horizontal = 16.dp)
                    .background(Color.Green)
                    .clickable{
                        if(!state.isDiscovering){
                            onAction(DeviceListAction.StartDiscovery)
                        }
                    }
            ){
                if(!state.isDiscovering){
                    Text("SCAN DEVICES")
                }else{
                    Text("STOP")
                }

            }
        }
    }
}