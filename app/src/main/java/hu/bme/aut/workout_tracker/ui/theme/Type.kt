package hu.bme.aut.workout_tracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

data class WorkoutTrackerTypography(
    val titleTextStyle: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 42.sp,
        textAlign = TextAlign.Center
    ),
    val secondaryButtonTextStyle: TextStyle = TextStyle(
        //fontWeight = FontWeight.SemiBold,
        //fontSize = 18.sp,
        textDecoration = TextDecoration.Underline
    ),
)

val LocalTypography = staticCompositionLocalOf { WorkoutTrackerTypography() }

val workoutTrackerTypography: WorkoutTrackerTypography
    @Composable
    @ReadOnlyComposable
    get() = staticCompositionLocalOf { WorkoutTrackerTypography() }.current