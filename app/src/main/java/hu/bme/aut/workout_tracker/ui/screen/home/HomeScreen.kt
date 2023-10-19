package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeInit
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.card.FavWorkoutCard
import hu.bme.aut.workout_tracker.ui.view.circularprogressindicator.WorkoutTrackerProgressIndicator

@Composable
fun HomeScreen(
    navigateToWorkout: (String) -> Unit,
    navigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val workouts by viewModel.workouts.observeAsState()

    viewModel.getFavoriteWorkouts()
    when (uiState) {
        HomeInit -> {
            viewModel.getFavoriteWorkouts()
        }

        HomeLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = workoutTrackerDimens.gapNormal,
                        end = workoutTrackerDimens.gapNormal,
                        bottom = 80.dp
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = null
                        )
                    }
                }
                Text(stringResource(R.string.home))
                if (workouts == null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WorkoutTrackerProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(
                            top = workoutTrackerDimens.gapNormal,
                            start = workoutTrackerDimens.gapNormal,
                            end = workoutTrackerDimens.gapNormal,
                        ),
                    ) {
                        if (workouts!!.isEmpty()) {
                            item {
                                Text(text = stringResource(R.string.no_favorite_workouts))
                            }
                        } else {
                            itemsIndexed(workouts!!) { i, w ->
                                FavWorkoutCard(
                                    name = w.name,
                                    exerciseNum = w.exercises.size,
                                    onClick = { navigateToWorkout(w.id) },
                                    modifier = Modifier
                                        .padding(
                                            top = workoutTrackerDimens.gapSmall,
                                            bottom = if (i == workouts!!.size - 1) workoutTrackerDimens.gapMedium else workoutTrackerDimens.gapSmall
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}