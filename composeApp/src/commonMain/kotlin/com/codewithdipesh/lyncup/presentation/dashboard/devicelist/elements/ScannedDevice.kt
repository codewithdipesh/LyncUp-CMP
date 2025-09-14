package com.codewithdipesh.lyncup.presentation.dashboard.devicelist.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.lyncup.Res
import com.codewithdipesh.lyncup.android_icon
import com.codewithdipesh.lyncup.apple_icon
import com.codewithdipesh.lyncup.domain.model.Device
import com.codewithdipesh.lyncup.domain.model.DeviceType
import com.codewithdipesh.lyncup.plus_icon
import com.codewithdipesh.lyncup.presentation.ui.regular
import com.codewithdipesh.lyncup.windows_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScannedDevice(
    device : Device,
    onConnectClick : (Device) -> Unit
){
    val icon = when(device.deviceType){
        DeviceType.ANDROID -> {
           Res.drawable.android_icon
        }
        DeviceType.DESKTOP -> {
           Res.drawable.windows_icon
        }
        DeviceType.IOS -> {
           Res.drawable.apple_icon
        }
    }
    Box(
        modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            //icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }

            //name
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                //device name
                Text(
                    text = device.name,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold
                    )
                )
                //ip
                Text(
                    text = "IP : " +device.ip,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                        fontSize = 12.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            //connect icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .clickable{
                        onConnectClick(device)
                    },
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(Res.drawable.plus_icon) ,
                    contentDescription = null
                )
            }

        }
    }
}