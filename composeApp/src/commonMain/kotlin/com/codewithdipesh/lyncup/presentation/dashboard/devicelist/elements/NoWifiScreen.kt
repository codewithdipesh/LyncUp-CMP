package com.codewithdipesh.lyncup.presentation.dashboard.devicelist.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.lyncup.Res
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.presentation.dashboard.devicelist.DeviceListAction
import com.codewithdipesh.lyncup.presentation.ui.regular
import com.codewithdipesh.lyncup.wifi_required
import org.jetbrains.compose.resources.painterResource

@Composable
fun NoWifiScreen(
    onAction: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(Res.drawable.wifi_required),
            contentDescription = null
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Oops!",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = regular,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "wifi required",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = regular,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Please connect to same network on both devices.",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                fontFamily = regular,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable{
                    onAction()
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Connect to Wifi",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}