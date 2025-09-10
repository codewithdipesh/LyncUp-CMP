package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.lyncup.Res
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.more_icon
import com.codewithdipesh.lyncup.settings_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeviceConnectionContent(
    state : DeviceListUI,
    onAction : (DeviceListAction) -> Unit,
    platform: PlatformType
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ){
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    //more icon
                    Box(
                        modifier = Modifier.size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable{
                                //todo
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painter = painterResource(Res.drawable.more_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //dashboard heading
                    Text(
                        text = "Dashboard",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    //settings icon
                    Box(
                        modifier = Modifier.size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable{
                                //todo
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painter = painterResource(Res.drawable.settings_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ){it->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
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
}