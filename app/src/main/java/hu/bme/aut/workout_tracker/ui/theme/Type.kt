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
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_medium, FontWeight.Medium)
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
        fontWeight = FontWeight.Bold,
        fontSize = 46.sp,
        textAlign = TextAlign.Center
    ),
    val workoutTitleTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        textAlign = TextAlign.Center
    ),

    val secondaryButtonTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        textDecoration = TextDecoration.Underline
    ),
    val triStateToggleTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    val workoutTableTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
    ),
    val editWorkoutTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
    ),
    val editWorkoutPlaceholderTextStyle: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
    ),
    val normal12sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    val normal14sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    val normal16sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    val medium12sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    ),
    val medium14sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    val medium16sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    val medium18sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    ),
    val medium20sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    val bold14sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
    val bold16sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    val bold24sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    val bold32sp: TextStyle = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
    ),

    )

val LocalTypography = staticCompositionLocalOf { WorkoutTrackerTypography() }

val workoutTrackerTypography: WorkoutTrackerTypography
    @Composable
    @ReadOnlyComposable
    get() = staticCompositionLocalOf { WorkoutTrackerTypography() }.current