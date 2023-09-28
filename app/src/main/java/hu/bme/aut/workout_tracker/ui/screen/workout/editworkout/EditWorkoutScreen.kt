package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutInit
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutLoaded
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutSaved
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.card.ExerciseCard

@Composable
fun EditWorkoutScreen(
    workoutId: String,
    navigateToAddExercise: (id: String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: EditWorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val workoutExercises by viewModel.workoutExercises.observeAsState()
    val updateWorkoutFailedEvent by viewModel.updateWorkoutFailedEvent.collectAsState()

    when (uiState) {
        EditWorkoutInit -> {
            viewModel.getWorkout(workoutId)
        }

        is EditWorkoutLoaded -> {
            viewModel.getWorkoutExercises()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapNormal),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text("Edit Workout")
                    TextField(
                        value = (uiState as EditWorkoutLoaded).name,
                        onValueChange = viewModel::onNameChange,
                        label = { Text(text = "Name") }
                    )
                    if (workoutExercises == null) {
                        //TODO("ProgressIndicator")
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(vertical = workoutTrackerDimens.gapNormal),
                        ) {
                            if (viewModel.exercises.isEmpty()) {
                                item {
                                    Text(text = "No exercises in this workout")
                                }
                            } else {
                                items(viewModel.exercises) { e ->
                                    ExerciseCard(
                                        text = e.name,
                                        withIcon = true,
                                        onRemoveClick = { viewModel.removeButtonOnClick(e) },
                                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapSmall)
                                    )
                                }
                            }
                            item {
                                AddButton(
                                    onClick = { navigateToAddExercise(workoutId) },
                                    text = "Add Exercise",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = workoutTrackerDimens.gapNormal)
                                )
                            }
                        }
                    }
                }
                PrimaryButton(
                    onClick = { viewModel.saveButtonOnClick(workoutId) },
                    text = "Save",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
                if (updateWorkoutFailedEvent.isUpdateWorkoutFailed) {
                    Toast.makeText(
                        LocalContext.current,
                        updateWorkoutFailedEvent.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.handledUpdateWorkoutFailedEvent()
                }
            }
        }
        EditWorkoutSaved -> {
            navigateBack()
        }
    }
}