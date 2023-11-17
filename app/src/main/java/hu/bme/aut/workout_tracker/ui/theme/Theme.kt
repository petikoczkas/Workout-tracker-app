package hu.bme.aut.workout_tracker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun WorkoutTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColors provides WorkoutTrackerColors(),
        LocalTypography provides WorkoutTrackerTypography()
    ) {
        val colorScheme = when {
            darkTheme -> LocalColors.current.darkColorScheme
            else -> LocalColors.current.lightColorScheme
        }
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    darkTheme
            }
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

}