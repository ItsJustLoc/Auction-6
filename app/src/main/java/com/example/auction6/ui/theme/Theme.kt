package com.example.auction6.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = RetroOrange,
    background = SurfaceDark,
    surface = CardDark,
    onSurface = RetroCream,
    onBackground = RetroCream
)

private val LightColorScheme = lightColorScheme(
    primary = RetroOrange,
    background = RetroCream,
    surface = RetroCard,
    onSurface = RetroInk,
    onBackground = RetroInk,
    onPrimary = RetroCream
)

@Composable
fun Auction6Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
