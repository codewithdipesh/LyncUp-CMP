package com.codewithdipesh.lyncup.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LyncUpColorScheme = lightColorScheme(
    background = background,
    surface = surface,

    primary = primary,
    primaryContainer = primaryVariant,
    secondary = secondary,

    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onSurface = onSurface,

    surfaceVariant = surfaceVariant,
    scrim = scrim
)

@Composable
fun LyncUpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LyncUpColorScheme,
        typography = typography,
        content = content
    )
}