package hu.bme.aut.workout_tracker.ui.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton

@Composable
fun AddExerciseDialog(
    newExercise: String,
    onNewExerciseChange: (String) -> Unit,
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onSaveButtonClick: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismissRequest(false) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(workoutTrackerDimens.gapNormal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = newExercise,
                    onValueChange = onNewExerciseChange,
                    label = { Text(text = stringResource(R.string.name)) },
                )
                PrimaryButton(
                    onClick = {
                        onDismissRequest(false)
                        onSaveButtonClick()
                    },
                    text = stringResource(R.string.save),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = workoutTrackerDimens.gapNormal)
                )
            }
        }
    }
}