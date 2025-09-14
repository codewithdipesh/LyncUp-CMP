package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.elements.DisConnectedScreen
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.elements.ScannedDevice
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.elements.TopBar
import com.codewithdipesh.lyncup.presentation.ui.regular
import com.codewithdipesh.lyncup.scan_icon
import com.codewithdipesh.lyncup.scanning_icon
import com.codewithdipesh.lyncup.settings_icon
import com.codewithdipesh.lyncup.woman_on_laptop
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeviceConnectionContent(
    state : DeviceListUI,
    onAction : (DeviceListAction) -> Unit,
    platform: PlatformType
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TopBar(
                platform = platform,
                onSettingsClick = {  },
                onMoreClick = {  }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { it ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){

                if( //desktop conditions
                    (platform == PlatformType.DESKTOP && state.connectedDevice == null && !state.deviceListShown) ||
                    //or mobile condition
                    (platform == PlatformType.MOBILE && state.connectedDevice == null && !state.isDiscovering && !state.deviceListShown )
                ){
                    DisConnectedScreen(
                        platform = platform,
                        state = state
                    )
                }
                //scanned device
                if(
                    (platform == PlatformType.DESKTOP && state.connectedDevice == null && state.devices.isNotEmpty()) ||
                    (platform == PlatformType.MOBILE && state.connectedDevice == null && state.isDiscovering || state.deviceListShown )
                ){
                    if(state.devices.isNotEmpty() && state.connectedDevice == null){
                        Spacer(Modifier.height(16.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = if(platform == PlatformType.MOBILE) 16.dp else 32.dp)
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ){
                            Text(
                                text = "Available Devices",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 14.sp,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            state.devices.forEach {
                                ScannedDevice(
                                    device = it,
                                    onConnectClick = { onAction(DeviceListAction.ConnectToDevice(it)) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
            //scan devices
            if (platform == PlatformType.MOBILE){
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp,vertical = 16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if(state.isDiscovering) MaterialTheme.colorScheme.primary.copy(0.3f)
                            else MaterialTheme.colorScheme.primary
                        )
                        .clickable{
                            if(!state.isDiscovering){
                                onAction(DeviceListAction.StartDiscovery)
                            }else{
                                onAction(DeviceListAction.StopDiscovery)
                            }
                        },
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 32.dp,
                            vertical = 16.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.scanning_icon),
                            modifier = Modifier.size(28.dp),
                            contentDescription = "scan",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = if (!state.isDiscovering) "Scan For Devices" else "Stop",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                }
            }

        }
    }

}