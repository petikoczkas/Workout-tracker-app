package hu.bme.aut.workout_tracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class WorkoutTrackerColors(

    val darkPrimary: Color = Color(0xFF5089BC),
    val darkBackground: Color = Color(0xFF0F1213),
    val darkSurfaceVariant: Color = Color(0xFF373840),
    val darkOnSurfaceVariant: Color = Color(0xFF9EA2AE),


    val tertiary: Color = Color(0xFF0B7F00),
    val error: Color = Color(0xFFCE0328),

    val gold: Color = Color(0xFFFFD700),
    val silver: Color = Color(0xFFC0C0C0),
    val bronze: Color = Color(0xFFCD7F32),

    val darkColorScheme: ColorScheme = darkColorScheme(
        primary = darkPrimary,
        onPrimary = darkBackground,
        background = darkBackground,
        surface = darkBackground,
        surfaceVariant = darkSurfaceVariant,
        onSurfaceVariant = darkOnSurfaceVariant,
        error = error,
        onError = darkBackground,
        tertiary = tertiary

    ),

    val lightColorScheme: ColorScheme = lightColorScheme(
        /*primary = purple40,
         secondary = purpleGrey40,
         tertiary = pink40*/
        error = error,
        tertiary = tertiary

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

val workoutTrackerColors: WorkoutTrackerColors
    @Composable
    @ReadOnlyComposable
    get() = staticCompositionLocalOf { WorkoutTrackerColors() }.current