package hu.bme.aut.workout_tracker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WorkoutTrackerDimens(
    val gapNone: Dp = 0.dp,
    val gapVeryTiny: Dp = 1.dp,
    val gapTiny: Dp = 2.dp,
    val gapSmall: Dp = 4.dp,
    val gapMedium: Dp = 8.dp,
    val gapNormal: Dp = 16.dp,
    val gapLarge: Dp = 24.dp,
    val gapVeryLarge: Dp = 32.dp,
    val gapVeryVeryLarge: Dp = 56.dp,
    val gapExtraLarge: Dp = 64.dp,

    val settingsImageSize: Dp = 200.dp,
    val settingsImageContentSize: Dp = 150.dp,

    val minButtonHeight: Dp = 45.dp,
    val primaryButtonCornerSize: Dp = 16.dp,

    val minWorkoutCardHeight: Dp = 100.dp,
    val minFavWorkoutCardHeight: Dp = 72.dp,
    val workoutCardCornerSize: Dp = 12.dp,

    val minUserCardHeight: Dp = 56.dp,
)

val workoutTrackerDimens: WorkoutTrackerDimens
    @Composable
    @ReadOnlyComposable
    get() = staticCompositionLocalOf { WorkoutTrackerDimens() }.current