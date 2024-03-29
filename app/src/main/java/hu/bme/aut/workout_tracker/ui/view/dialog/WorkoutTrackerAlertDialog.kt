package hu.bme.aut.workout_tracker.ui.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun WorkoutTrackerAlertDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm)
            { Text(text = stringResource(id = R.string.ok)) }
        },
        title = {
            Text(
                text = title,
                style = workoutTrackerTypography.bold24sp
            )
        },
        text = {
            Text(
                text = description,
                style = workoutTrackerTypography.normal16sp

            )
        }
    )
}