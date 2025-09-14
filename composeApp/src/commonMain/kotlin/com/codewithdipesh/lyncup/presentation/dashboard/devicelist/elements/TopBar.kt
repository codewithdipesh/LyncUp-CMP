package com.codewithdipesh.lyncup.presentation.dashboard.devicelist.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.codewithdipesh.lyncup.domain.model.PlatformType
import com.codewithdipesh.lyncup.more_icon
import com.codewithdipesh.lyncup.presentation.ui.regular
import com.codewithdipesh.lyncup.settings_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopBar(
    platform: PlatformType,
    onSettingsClick: () -> Unit ,
    onMoreClick: () -> Unit
){
    //size
    val iconSize = if(platform == PlatformType.DESKTOP ) 14.dp else 16.dp
    val iconBox = if(platform == PlatformType.DESKTOP ) 28.dp else 32.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = if(platform == PlatformType.DESKTOP) 16.dp else 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            //more icon
            Box(
                modifier = Modifier.size(iconBox)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable{
                        onMoreClick()
                    },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(Res.drawable.more_icon),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            //dashboard heading
            Text(
                text = "Dashboard",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold
                )
            )

            //settings icon
            Box(
                modifier = Modifier.size(iconBox)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable{
                        onSettingsClick()
                    },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(Res.drawable.settings_icon),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize + 4.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}