package com.codewithdipesh.lyncup.presentation.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.codewithdipesh.lyncup.Res
import com.codewithdipesh.lyncup.roboto_bold
import com.codewithdipesh.lyncup.roboto_medium
import com.codewithdipesh.lyncup.roboto_regular
import org.jetbrains.compose.resources.Font


val regular  @Composable get() = FontFamily(
    Font(Res.font.roboto_regular, FontWeight.Normal),
    Font(Res.font.roboto_medium, FontWeight.Medium),
    Font(Res.font.roboto_bold, FontWeight.Bold)
)

val typography: Typography  @Composable get() = Typography(
    bodyMedium = TextStyle(
        fontFamily = regular,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )
)