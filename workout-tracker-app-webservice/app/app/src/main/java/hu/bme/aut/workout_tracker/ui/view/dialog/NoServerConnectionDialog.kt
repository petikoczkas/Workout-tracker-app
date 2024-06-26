package hu.bme.aut.workout_tracker.ui.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun NoServerConnectionDialog(
    isAvailable: Boolean
) {
    if (!isAvailable) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = {
                Text(
                    text = stringResource(R.string.server_connection_error_message),
                    style = workoutTrackerTypography.bold24sp
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.server_connection_error_description),
                    style = workoutTrackerTypography.normal16sp

                )
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}