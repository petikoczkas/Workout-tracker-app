package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutInit
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutLoaded
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutSaved
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.card.ExerciseCard
import hu.bme.aut.workout_tracker.ui.view.dialog.WorkoutTrackerAlertDialog
import hu.bme.aut.workout_tracker.ui.view.textfield.EditWorkoutTextField

@Composable
fun EditWorkoutScreen(
    workoutId: Int,
    navigateToAddExercise: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: EditWorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
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
                    .padding(horizontal = workoutTrackerDimens.gapLarge),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(R.string.edit_workout),
                        style = workoutTrackerTypography.titleTextStyle,
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    EditWorkoutTextField(
                        text = (uiState as EditWorkoutLoaded).name,
                        onTextChange = viewModel::onNameChange,
                        placeholder = stringResource(R.string.workout_name)
                    )
                    LazyColumn(
                        modifier = Modifier.padding(top = workoutTrackerDimens.gapNormal),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (viewModel.exercises.isEmpty()) {
                            item {
                                Text(text = stringResource(R.string.no_exercises))
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
                                text = stringResource(R.string.add_exercise),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = workoutTrackerDimens.gapNormal,
                                        bottom = workoutTrackerDimens.gapSmall
                                    )
                            )
                        }
                    }
                }
                PrimaryButton(
                    onClick = { viewModel.saveButtonOnClick(workoutId) },
                    text = stringResource(R.string.save),
                    enabled = (uiState as EditWorkoutLoaded).name.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = workoutTrackerDimens.gapNormal)
                )
                if (updateWorkoutFailedEvent.isUpdateWorkoutFailed) {
                    WorkoutTrackerAlertDialog(
                        title = stringResource(R.string.workout_update_failed),
                        description = updateWorkoutFailedEvent.exception?.message.toString(),
                        onDismiss = { viewModel.handledUpdateWorkoutFailedEvent() }
                    )
                }
            }
        }
        EditWorkoutSaved -> {
            navigateBack()
        }
    }
}