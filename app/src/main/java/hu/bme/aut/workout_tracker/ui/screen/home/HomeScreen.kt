package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeInit
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.card.FavWorkoutCard

@Composable
fun HomeScreen(
    navigateToWorkout: () -> Unit,
    navigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val workouts by viewModel.workouts.observeAsState()

    when (uiState) {
        HomeInit -> {
            viewModel.getWorkouts()
        }

        HomeLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapNormal),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { navigateToSettings() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = null
                        )
                    }
                }
                Text("Home")
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
                                FavWorkoutCard(
                                    name = w.name,
                                    exerciseNum = w.exercises.size,
                                    onClick = navigateToWorkout,
                                    modifier = Modifier.padding(
                                        horizontal = workoutTrackerDimens.gapNormal,
                                        vertical = workoutTrackerDimens.gapSmall
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