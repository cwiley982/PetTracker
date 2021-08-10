package com.caitlynwiley.pettracker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

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
    primary = primaryDark_night,
    secondary = secondary_night,
    secondaryVariant = secondary_night, // intentionally the same as secondary
    background = backgroundColor_night,
    surface = surfaceColor_night,
    error = errorColor_night,
    onPrimary = primaryTextColor_night,
    onSecondary = secondaryTextColor_night,
    onBackground = secondaryTextColor_night,
    onSurface = secondaryTextColor_night,
    onError = primaryTextColor_night
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