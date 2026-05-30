package com.mgpark.trackbox.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary           = Indigo500,
    onPrimary         = Color.White,
    primaryContainer  = Indigo100,
    onPrimaryContainer= Indigo900,
    secondary         = Slate600,
    onSecondary       = Color.White,
    secondaryContainer= Slate100,
    onSecondaryContainer = Slate900,
    tertiary          = Emerald500,
    onTertiary        = Color.White,
    background        = Slate50,
    onBackground      = Slate900,
    surface           = Surface,
    onSurface         = Slate900,
    surfaceVariant    = Slate100,
    onSurfaceVariant  = Slate600,
    outline           = Slate300,
    outlineVariant    = Slate200,
    error             = Rose600,
    onError           = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary           = Indigo300,
    onPrimary         = Indigo900,
    primaryContainer  = IndigoContainerDark,
    onPrimaryContainer= Indigo100,
    secondary         = Slate300,
    onSecondary       = Slate900,
    secondaryContainer= Slate700,
    onSecondaryContainer = Slate50,
    tertiary          = Emerald400,
    onTertiary        = Slate950,
    background        = Slate950,
    onBackground      = Slate50,
    surface           = SurfaceDark,
    onSurface         = Slate50,
    surfaceVariant    = Slate800,
    onSurfaceVariant  = Slate300,
    outline           = Slate700,
    outlineVariant    = Slate800,
    error             = Rose500,
    onError           = Color.White,
)

@Composable
fun TrackBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }
    val deliveryColors = if (darkTheme) DarkDeliveryColors else LightDeliveryColors

    CompositionLocalProvider(LocalDeliveryColors provides deliveryColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = AppTypography,
            shapes      = AppShapes,
            content     = content,
        )
    }
}
