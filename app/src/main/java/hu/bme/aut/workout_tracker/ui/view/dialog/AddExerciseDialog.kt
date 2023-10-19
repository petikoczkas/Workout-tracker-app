package hu.bme.aut.workout_tracker.ui.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton

@Composable
fun AddExerciseDialog(
    newExercise: String,
    onNewExerciseChange: (String) -> Unit,
    selectedItem: String,
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onSaveButtonClick: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismissRequest(false) }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor,
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(workoutTrackerDimens.gapNormal)) {
                    Text(
                        text = "Body Part: $selectedItem",
                        style = TextStyle(fontSize = 24.sp)
                    )
                    TextField(
                        value = newExercise,
                        onValueChange = onNewExerciseChange,
                        label = { Text(text = stringResource(R.string.name)) },
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapNormal)
                    )
                    PrimaryButton(
                        onClick = {
                            onDismissRequest(false)
                            onSaveButtonClick()
                        },
                        text = stringResource(R.string.save),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}