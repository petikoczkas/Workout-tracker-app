package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseInit
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.card.ExerciseCard
import hu.bme.aut.workout_tracker.ui.view.dialog.AddExerciseDialog
import hu.bme.aut.workout_tracker.ui.view.dropdownmenu.WorkoutTrackerDropDownMenu
import hu.bme.aut.workout_tracker.utils.Constants

@Composable
fun AddExerciseScreen(
    workoutId: String,
    navigateBack: () -> Unit,
    viewModel: AddExerciseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val exercises by viewModel.exercises.observeAsState()
    val createExerciseFailedEvent by viewModel.createExerciseFailedEvent.collectAsState()

    when (uiState) {
        AddExerciseInit -> {
            viewModel.getExercises(workoutId)
        }

        is AddExerciseLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapNormal),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text("Add Exercise")

                    WorkoutTrackerDropDownMenu(
                        selectedItem = (uiState as AddExerciseLoaded).selectedItem,
                        onSelectedItemChange = viewModel::onSelectedItemChange,
                        items = Constants.BODY_PARTS,
                        modifier = Modifier.padding(workoutTrackerDimens.gapNormal)
                    )
                    if (exercises == null) {
                        //TODO("ProgressIndicator")
                    } else {
                        AddExerciseDialog(
                            newExercise = (uiState as AddExerciseLoaded).newExercise,
                            onNewExerciseChange = viewModel::onNewExerciseChange,
                            showDialog = (uiState as AddExerciseLoaded).showDialog,
                            onDismissRequest = viewModel::onShowDialogChange,
                            onSaveButtonClick = { viewModel.dialogSaveButtonOnClick(exercises!!) }
                        )

                        val categoryList = viewModel.getExercisesByCategory(exercises)
                        LazyColumn(
                            modifier = Modifier.padding(top = workoutTrackerDimens.gapNormal),
                        ) {
                            if (categoryList.isEmpty()) {
                                item {
                                    Text(text = "There is no exercise in this category")
                                }
                            } else {
                                items(categoryList) { e ->
                                    ExerciseCard(
                                        text = e.name,
                                        onClick = {
                                            viewModel.saveExercise(e)
                                            navigateBack()

                                        },
                                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapSmall)
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
                        .padding(vertical = workoutTrackerDimens.gapNormal)
                )
                if (createExerciseFailedEvent.isCreateExerciseFailed) {
                    Toast.makeText(
                        LocalContext.current,
                        createExerciseFailedEvent.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.handledCreateExerciseFailedEvent()
                }
            }
        }
    }
}