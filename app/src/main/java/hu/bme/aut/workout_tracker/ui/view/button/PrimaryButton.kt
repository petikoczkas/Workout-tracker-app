package hu.bme.aut.workout_tracker.ui.view.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(workoutTrackerDimens.minButtonHeight),
        /*colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colorScheme.background,
            disabledBackgroundColor = MaterialTheme.colorScheme.primaryVariant,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary

        ),*/
        enabled = enabled,
        shape = RoundedCornerShape(workoutTrackerDimens.primaryButtonCornerSize),
    ) {
        Text(
            text = text,
            style = workoutTrackerTypography.primaryButtonTextStyle
        )
    }
}