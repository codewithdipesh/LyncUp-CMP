package com.codewithdipesh.lyncup.presentation.dashboard.devicelist.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.lyncup.Res
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceListUI
import com.codewithdipesh.lyncup.presentation.ui.regular
import com.codewithdipesh.lyncup.woman_on_laptop
import org.jetbrains.compose.resources.painterResource

@Composable
fun DisConnectedScreen(
    platform: PlatformType,
    state: DeviceListUI
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(Res.drawable.woman_on_laptop),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "Your Mobile and Desktop working as one",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.65f),
                    fontSize = 12.sp,
                    fontFamily = regular
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            if(platform == PlatformType.DESKTOP && state.connectedDevice == null
                && state.isDiscovering
            ){
                Text(
                    text = "Discovering Devices...",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                        fontFamily = regular
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}