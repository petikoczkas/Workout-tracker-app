package hu.bme.aut.workout_tracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class WorkoutTrackerColors(

    private val purple80: Color = Color(0xFFD0BCFF),
    private val purpleGrey80: Color = Color(0xFFCCC2DC),
    private val pink80: Color = Color(0xFFEFB8C8),

    private val purple40: Color = Color(0xFF6650a4),
    private val purpleGrey40: Color = Color(0xFF625b71),
    private val pink40: Color = Color(0xFF7D5260),

    val darkColorScheme: ColorScheme = darkColorScheme(
        primary = purple80,
        secondary = purpleGrey80,
        tertiary = pink80
    ),

    val lightColorScheme: ColorScheme = lightColorScheme(
        primary = purple40,
        secondary = purpleGrey40,
        tertiary = pink40

        /* Other default colors to override
        background = Color(0xFFFFFBFE),
        surface = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
        */
    )
)

val LocalColors = staticCompositionLocalOf { WorkoutTrackerColors() }