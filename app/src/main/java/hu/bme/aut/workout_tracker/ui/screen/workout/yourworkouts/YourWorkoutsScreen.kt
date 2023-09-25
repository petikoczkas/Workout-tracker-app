package hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsUiState.YourWorkoutsInit
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsUiState.YourWorkoutsLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.card.WorkoutCard

@Composable
fun YourWorkoutsScreen(
    navigateToWorkout: () -> Unit,
    navigateToEditWorkout: (id: String) -> Unit,
    navigateToCreateWorkout: () -> Unit,
    viewModel: YourWorkoutsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val workouts by viewModel.workouts.observeAsState()

    when (uiState) {
        YourWorkoutsInit -> {
            viewModel.getWorkouts()
        }

        YourWorkoutsLoaded -> {
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
                    Text("Your Workouts")
                    if (workouts == null) {
                        //TODO("ProgressIndicator")
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(vertical = workoutTrackerDimens.gapNormal),
                        ) {
                            if (workouts!!.isEmpty()) {
                                item {
                                    Text(text = "You have no workouts")
                                }
                            } else {
                                items(workouts!!) { w ->
                                    WorkoutCard(
                                        name = w.name,
                                        exerciseNum = w.exercises.size,
                                        onClick = navigateToWorkout,
                                        onEditClick = { navigateToEditWorkout(w.id) },
                                        isFav = viewModel.isFavorite(w.id),
                                        onFavClick = { viewModel.isFavoriteOnClick(w.id) }
                                    )
                                }
                            }
                        }
                    }
                }
                AddButton(
                    onClick = navigateToCreateWorkout,
                    text = "Create Workout",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 80.dp + workoutTrackerDimens.gapNormal)
                )
            }
        }
    }
}