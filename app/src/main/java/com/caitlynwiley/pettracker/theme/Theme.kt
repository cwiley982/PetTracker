package com.caitlynwiley.pettracker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = primaryColor_day,
    primaryVariant = primaryDark_day,
    secondary = secondaryColor_day,
    secondaryVariant = secondaryDark_day,
    background = secondaryLight_day,
    surface = secondaryDark_day,
    error = errorColor_day,
    onPrimary = primaryTextColor_day,
    onSecondary = secondaryTextColor_day,
    onBackground = secondaryTextColor_day,
    onSurface = secondaryTextColor_day,
    onError = secondaryTextColor_day
)

private val DarkColorPalette = darkColors(
    primary = primaryColor_night,
    primaryVariant = primaryTransparent_night,
    secondary = secondaryColor_night,
    secondaryVariant = secondaryColor_night,
    background = backgroundColor_night,
    surface = surfaceColor_night,
    error = errorColor_night,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun PetTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit)
{
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}