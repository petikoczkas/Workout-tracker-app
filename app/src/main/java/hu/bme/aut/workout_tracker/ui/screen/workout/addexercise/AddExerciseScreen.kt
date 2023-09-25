package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseInit
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseLoaded
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseSaved
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.card.ExerciseCard
import hu.bme.aut.workout_tracker.ui.view.dialog.AddExerciseDialog
import hu.bme.aut.workout_tracker.ui.view.dropdownmenu.WorkoutTrackerDropDownMenu

@Composable
fun AddExerciseScreen(
    viewModel: AddExerciseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val exercises by viewModel.exercises.observeAsState()

    when (uiState) {
        AddExerciseInit -> {
            viewModel.getExercises()
        }

        is AddExerciseLoaded -> {
            AddExerciseDialog(
                newExercise = (uiState as AddExerciseLoaded).newExercise,
                onNewExerciseChange = viewModel::onNewExerciseChange,
                showDialog = (uiState as AddExerciseLoaded).showDialog,
                onDismissRequest = viewModel::onShowDialogChange,
                onSaveButtonClick = { }
            )
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
                    Text("Add Exercise")

                    WorkoutTrackerDropDownMenu(
                        selectedItem = (uiState as AddExerciseLoaded).selectedItem,
                        onSelectedItemChange = viewModel::onSelectedItemChange
                    )

                    if (exercises == null) {
                        //TODO("ProgressIndicator")
                    } else {
                        val categoryList = viewModel.getExercisesByCategory(exercises)
                        LazyColumn(
                            modifier = Modifier.padding(vertical = workoutTrackerDimens.gapNormal),
                        ) {
                            if (categoryList.isEmpty()) {
                                item {
                                    Text(text = "You have no workouts")
                                }
                            } else {
                                items(categoryList) { e ->
                                    ExerciseCard(
                                        text = e.name,
                                        onClick = { }
                                    )
                                }
                            }
                        }
                    }
                }
                AddButton(
                    onClick = { viewModel.onShowDialogChange(true) },
                    text = "Create a custom exercise",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
            }
        }

        AddExerciseSaved -> TODO()
    }
}