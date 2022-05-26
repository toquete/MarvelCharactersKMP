package com.guilherme.marvelcharacters.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Red500,
    primaryVariant = Red700,
    secondary = Grey500,
    secondaryVariant = Grey800
)

private val DarkColorPalette = darkColors(
    primary = Red200,
    primaryVariant = Red500,
    secondary = Red200,
    secondaryVariant = Grey800,
    error = Red200,
    background = Color.Black
)

@Composable
fun MarvelCharactersTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}