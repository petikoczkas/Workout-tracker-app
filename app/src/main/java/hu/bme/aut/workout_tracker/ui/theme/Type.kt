package hu.bme.aut.workout_tracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import hu.bme.aut.workout_tracker.R

// Set of Material typography styles to start with
val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.SemiBold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

data class WorkoutTrackerTypography(
    val titleTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.SemiBold,
        fontSize = 46.sp,
        textAlign = TextAlign.Center
    ),
    val secondaryButtonTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        //fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        textDecoration = TextDecoration.Underline
    ),
    val workoutTrackerTextFieldTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    val primaryButtonTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    val passwordCheckerTitleTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    val passwordCheckerDescriptionTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    ),
)

val LocalTypography = staticCompositionLocalOf { WorkoutTrackerTypography() }

val workoutTrackerTypography: WorkoutTrackerTypography
    @Composable
    @ReadOnlyComposable
    get() = staticCompositionLocalOf { WorkoutTrackerTypography() }.current