package com.kelompok2.hydratrack.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1769AA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8ECFA),
    onPrimaryContainer = Color(0xFF164B73),
    background = Color(0xFFEAF6FF),
    onBackground = Color(0xFF164B73),
    surface = Color.White,
    onSurface = Color(0xFF164B73),
    surfaceVariant = Color(0xFFDDECF5),
    onSurfaceVariant = Color(0xFF426982),
    outline = Color(0xFFB6D6EA)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8CCBFF),
    onPrimary = Color(0xFF003354),
    primaryContainer = Color(0xFF164B73),
    onPrimaryContainer = Color(0xFFD8ECFA),
    background = Color(0xFF101A24),
    onBackground = Color(0xFFE2F2FF),
    surface = Color(0xFF182735),
    onSurface = Color(0xFFE2F2FF),
    surfaceVariant = Color(0xFF263B4C),
    onSurfaceVariant = Color(0xFFC0D5E4),
    outline = Color(0xFF466276)
)

@Composable
fun HydraTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
