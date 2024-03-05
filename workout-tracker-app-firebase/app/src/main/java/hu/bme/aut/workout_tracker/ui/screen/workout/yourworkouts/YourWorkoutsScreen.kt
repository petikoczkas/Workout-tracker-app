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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsUiState.YourWorkoutsInit
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsUiState.YourWorkoutsLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.card.WorkoutCard
import hu.bme.aut.workout_tracker.ui.view.circularprogressindicator.WorkoutTrackerProgressIndicator

@Composable
fun YourWorkoutsScreen(
    navigateToWorkout: (String) -> Unit,
    navigateToEditWorkout: (String) -> Unit,
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
                        text = stringResource(R.string.your_workouts),
                        style = workoutTrackerTypography.titleTextStyle,
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    if (workouts == null) {
                        WorkoutTrackerProgressIndicator()
                    } else {
                        LazyColumn {
                            if (workouts!!.isEmpty()) {
                                item {
                                    Text(text = stringResource(R.string.you_have_no_workouts))
                                }
                            } else {
                                items(workouts!!) { w ->
                                    WorkoutCard(
                                        name = w.name,
                                        exerciseNum = w.exercises.size,
                                        onClick = { navigateToWorkout(w.id) },
                                        onEditClick = { navigateToEditWorkout(w.id) },
                                        isFav = viewModel.isFavorite(w.id),
                                        onFavClick = { viewModel.isFavoriteOnClick(w.id) },
                                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapSmall)
                                    )
                                }
                            }
                        }
                    }
                }
                AddButton(
                    onClick = navigateToCreateWorkout,
                    text = stringResource(R.string.create_workout),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = workoutTrackerDimens.gapMedium,
                            bottom = workoutTrackerDimens.bottomNavigationBarHeight + workoutTrackerDimens.gapNormal
                        )
                )
            }
        }
    }
}