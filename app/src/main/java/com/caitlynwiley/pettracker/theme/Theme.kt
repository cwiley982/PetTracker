package com.caitlynwiley.pettracker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.caitlynwiley.pettracker.*

private val LightColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = primaryDarkVariant,
    secondary = secondaryColor,
    secondaryVariant = secondaryDarkVariant,
    background = primaryLightVariant,
    surface = secondaryLightVariant,
    error = errorColor,
    onPrimary = primaryTextColor,
    onSecondary = secondaryTextColor,
    onBackground = primaryTextColor,
    onSurface = primaryTextColor,
    onError = primaryTextColorNight
)

private val DarkColorPalette = lightColors(
    primary = primaryColorNight,
    primaryVariant = primaryDarkVariantNight,
    secondary = secondaryColorNight,
    secondaryVariant = secondaryDarkVariantNight,
    background = primaryLightVariantNight,
    surface = secondaryLightVariantNight,
    error = errorColorNight,
    onPrimary = primaryTextColorNight,
    onSecondary = secondaryTextColorNight,
    onBackground = primaryTextColorNight,
    onSurface = primaryTextColorNight,
    onError = primaryTextColor
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