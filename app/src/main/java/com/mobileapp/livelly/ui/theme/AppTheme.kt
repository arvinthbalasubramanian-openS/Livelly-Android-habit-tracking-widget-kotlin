package com.mobileapp.livelly.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(

    primary = Accent,

    background = DarkBackground,

    surface = DarkSurface,

    onPrimary = TextLight,

    onBackground = TextLight,

    onSurface = TextLight
)

private val LightColors = lightColorScheme(

    primary = LightAccent,

    background = LightBackground,

    surface = LightSurface,

    onPrimary = TextLight,

    onBackground = TextDark,

    onSurface = TextDark
)

@Composable
fun LivellyTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme =
            if (darkTheme)
                DarkColors
            else
                LightColors,

        content = content
    )
}