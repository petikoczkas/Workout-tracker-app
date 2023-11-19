package hu.bme.aut.workout_tracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class WorkoutTrackerColors(

    val md_theme_light_primary: Color = Color(0xFF335CA8),
    val md_theme_light_onPrimary: Color = Color(0xFFFFFFFF),
    val md_theme_light_primaryContainer: Color = Color(0xFFD8E2FF),
    val md_theme_light_onPrimaryContainer: Color = Color(0xFF001A42),
    val md_theme_light_secondary: Color = Color(0xFF385BA9),
    val md_theme_light_onSecondary: Color = Color(0xFFFFFFFF),
    val md_theme_light_secondaryContainer: Color = Color(0xFFDAE2FF),
    val md_theme_light_onSecondaryContainer: Color = Color(0xFF001946),
    val md_theme_light_tertiary: Color = Color(0xFF3A5BA9),
    val md_theme_light_onTertiary: Color = Color(0xFFFFFFFF),
    val md_theme_light_tertiaryContainer: Color = Color(0xFFDAE2FF),
    val md_theme_light_onTertiaryContainer: Color = Color(0xFF001847),
    val md_theme_light_error: Color = Color(0xFFBA1A1A),
    val md_theme_light_errorContainer: Color = Color(0xFFFFDAD6),
    val md_theme_light_onError: Color = Color(0xFFFFFFFF),
    val md_theme_light_onErrorContainer: Color = Color(0xFF410002),
    val md_theme_light_background: Color = Color(0xFFF8FDFF),
    val md_theme_light_onBackground: Color = Color(0xFF001F25),
    val md_theme_light_surface: Color = Color(0xFFF8FDFF),
    val md_theme_light_onSurface: Color = Color(0xFF001F25),
    val md_theme_light_surfaceVariant: Color = Color(0xFFE1E2EC),
    val md_theme_light_onSurfaceVariant: Color = Color(0xFF44474F),
    val md_theme_light_outline: Color = Color(0xFF75777F),
    val md_theme_light_inverseOnSurface: Color = Color(0xFFD6F6FF),
    val md_theme_light_inverseSurface: Color = Color(0xFF00363F),
    val md_theme_light_inversePrimary: Color = Color(0xFFAEC6FF),
    val md_theme_light_shadow: Color = Color(0xFF000000),
    val md_theme_light_surfaceTint: Color = Color(0xFF335CA8),
    val md_theme_light_outlineVariant: Color = Color(0xFFC5C6D0),
    val md_theme_light_scrim: Color = Color(0xFF000000),

    val md_theme_dark_primary: Color = Color(0xFFAEC6FF),
    val md_theme_dark_onPrimary: Color = Color(0xFF002E6B),
    val md_theme_dark_primaryContainer: Color = Color(0xFF12448F),
    val md_theme_dark_onPrimaryContainer: Color = Color(0xFFD8E2FF),
    val md_theme_dark_secondary: Color = Color(0xFFB1C5FF),
    val md_theme_dark_onSecondary: Color = Color(0xFF002C70),
    val md_theme_dark_secondaryContainer: Color = Color(0xFF1B438F),
    val md_theme_dark_onSecondaryContainer: Color = Color(0xFFDAE2FF),
    val md_theme_dark_tertiary: Color = Color(0xFFB2C5FF),
    val md_theme_dark_onTertiary: Color = Color(0xFF002C72),
    val md_theme_dark_tertiaryContainer: Color = Color(0xFF1D438F),
    val md_theme_dark_onTertiaryContainer: Color = Color(0xFFDAE2FF),
    val md_theme_dark_error: Color = Color(0xFFFFB4AB),
    val md_theme_dark_errorContainer: Color = Color(0xFF93000A),
    val md_theme_dark_onError: Color = Color(0xFF690005),
    val md_theme_dark_onErrorContainer: Color = Color(0xFFFFDAD6),
    val md_theme_dark_background: Color = Color(0xFF001F25),
    val md_theme_dark_onBackground: Color = Color(0xFFA6EEFF),
    val md_theme_dark_surface: Color = Color(0xFF001F25),
    val md_theme_dark_onSurface: Color = Color(0xFFA6EEFF),
    val md_theme_dark_surfaceVariant: Color = Color(0xFF44474F),
    val md_theme_dark_onSurfaceVariant: Color = Color(0xFFC5C6D0),
    val md_theme_dark_outline: Color = Color(0xFF8E9099),
    val md_theme_dark_inverseOnSurface: Color = Color(0xFF001F25),
    val md_theme_dark_inverseSurface: Color = Color(0xFFA6EEFF),
    val md_theme_dark_inversePrimary: Color = Color(0xFF335CA8),
    val md_theme_dark_shadow: Color = Color(0xFF000000),
    val md_theme_dark_surfaceTint: Color = Color(0xFFAEC6FF),
    val md_theme_dark_outlineVariant: Color = Color(0xFF44474F),
    val md_theme_dark_scrim: Color = Color(0xFF000000),


    //val tertiary: Color = Color(0xFF0B7F00),
    val gold: Color = Color(0xFFFFD700),
    val silver: Color = Color(0xFFC0C0C0),
    val bronze: Color = Color(0xFFCD7F32),

    val darkColorScheme: ColorScheme = darkColorScheme(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_onPrimary,
        primaryContainer = md_theme_dark_primaryContainer,
        onPrimaryContainer = md_theme_dark_onPrimaryContainer,
        secondary = md_theme_dark_secondary,
        onSecondary = md_theme_dark_onSecondary,
        secondaryContainer = md_theme_dark_secondaryContainer,
        onSecondaryContainer = md_theme_dark_onSecondaryContainer,
        tertiary = md_theme_dark_tertiary,
        onTertiary = md_theme_dark_onTertiary,
        tertiaryContainer = md_theme_dark_tertiaryContainer,
        onTertiaryContainer = md_theme_dark_onTertiaryContainer,
        error = md_theme_dark_error,
        errorContainer = md_theme_dark_errorContainer,
        onError = md_theme_dark_onError,
        onErrorContainer = md_theme_dark_onErrorContainer,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_onBackground,
        surface = md_theme_dark_surface,
        onSurface = md_theme_dark_onSurface,
        surfaceVariant = md_theme_dark_surfaceVariant,
        onSurfaceVariant = md_theme_dark_onSurfaceVariant,
        outline = md_theme_dark_outline,
        inverseOnSurface = md_theme_dark_inverseOnSurface,
        inverseSurface = md_theme_dark_inverseSurface,
        inversePrimary = md_theme_dark_inversePrimary,
        surfaceTint = md_theme_dark_surfaceTint,
        outlineVariant = md_theme_dark_outlineVariant,
        scrim = md_theme_dark_scrim,

        ),

    val lightColorScheme: ColorScheme = lightColorScheme(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_onPrimary,
        primaryContainer = md_theme_light_primaryContainer,
        onPrimaryContainer = md_theme_light_onPrimaryContainer,
        secondary = md_theme_light_secondary,
        onSecondary = md_theme_light_onSecondary,
        secondaryContainer = md_theme_light_secondaryContainer,
        onSecondaryContainer = md_theme_light_onSecondaryContainer,
        tertiary = md_theme_light_tertiary,
        onTertiary = md_theme_light_onTertiary,
        tertiaryContainer = md_theme_light_tertiaryContainer,
        onTertiaryContainer = md_theme_light_onTertiaryContainer,
        error = md_theme_light_error,
        errorContainer = md_theme_light_errorContainer,
        onError = md_theme_light_onError,
        onErrorContainer = md_theme_light_onErrorContainer,
        background = md_theme_light_background,
        onBackground = md_theme_light_onBackground,
        surface = md_theme_light_surface,
        onSurface = md_theme_light_onSurface,
        surfaceVariant = md_theme_light_surfaceVariant,
        onSurfaceVariant = md_theme_light_onSurfaceVariant,
        outline = md_theme_light_outline,
        inverseOnSurface = md_theme_light_inverseOnSurface,
        inverseSurface = md_theme_light_inverseSurface,
        inversePrimary = md_theme_light_inversePrimary,
        surfaceTint = md_theme_light_surfaceTint,
        outlineVariant = md_theme_light_outlineVariant,
        scrim = md_theme_light_scrim,
    )
)

val LocalColors = staticCompositionLocalOf { WorkoutTrackerColors() }

val workoutTrackerColors: WorkoutTrackerColors
    @Composable
    @ReadOnlyComposable
    get() = staticCompositionLocalOf { WorkoutTrackerColors() }.current