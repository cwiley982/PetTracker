package com.caitlynwiley.pettracker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = primaryColor_Light,
    primaryVariant = primaryDark_Light,
    secondary = secondaryColor_Light,
    secondaryVariant = secondaryDark_Light,
    background = secondaryLight_Light,
    surface = secondaryDark_Light,
    error = errorColor_Light,
    onPrimary = primaryTextColor_Light,
    onSecondary = secondaryTextColor_Light,
    onBackground = secondaryTextColor_Light,
    onSurface = secondaryTextColor_Light,
    onError = secondaryTextColor_Light
)

private val DarkColorPalette = darkColors(
    primary = primaryDark_Dark,
    secondary = secondaryColor_Dark,
    secondaryVariant = secondaryDark_Dark,
    background = secondaryLight_Dark,
    surface = secondaryDark_Dark,
    error = errorColor_Dark,
    onPrimary = primaryTextColor_Dark,
    onSecondary = secondaryTextColor_Dark,
    onBackground = secondaryTextColor_Dark,
    onSurface = secondaryTextColor_Dark,
    onError = primaryTextColor_Dark
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