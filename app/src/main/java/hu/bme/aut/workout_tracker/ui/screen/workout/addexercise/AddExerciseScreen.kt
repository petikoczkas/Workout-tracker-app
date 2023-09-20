package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton

@Composable
fun AddExerciseScreen(

) {
    var showDialog by remember { mutableStateOf(false) }
    var exercise by rememberSaveable { mutableStateOf("") }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(workoutTrackerDimens.gapNormal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = exercise,
                    onValueChange = { exercise = it },
                    label = { Text(text = "Name") },
                )
                PrimaryButton(
                    onClick = { showDialog = false },
                    text = "Save",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = workoutTrackerDimens.gapNormal)
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapNormal),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Add Exercise")
        AddButton(
            onClick = { showDialog = true },
            text = "Create a custom exercise",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = workoutTrackerDimens.gapNormal)
        )
    }
}