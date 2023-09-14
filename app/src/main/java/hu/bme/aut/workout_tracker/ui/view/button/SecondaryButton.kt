package hu.bme.aut.workout_tracker.ui.view.button

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    text: String
) {
    TextButton(
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = workoutTrackerTypography.secondaryButtonTextStyle,
        )
    }
}