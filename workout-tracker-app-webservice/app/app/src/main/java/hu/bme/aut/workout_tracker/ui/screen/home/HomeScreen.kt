package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeInit
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.card.FavWorkoutCard
import hu.bme.aut.workout_tracker.ui.view.card.OneRepMaxCard
import hu.bme.aut.workout_tracker.ui.view.circularprogressindicator.WorkoutTrackerProgressIndicator

@Composable
fun HomeScreen(
    navigateToWorkout: (String) -> Unit,
    navigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val workouts by viewModel.workouts.observeAsState()
    val exercises by viewModel.exercises.observeAsState()

    viewModel.getFavoriteWorkouts()
    when (uiState) {
        HomeInit -> {
            viewModel.getFavoriteWorkouts()
        }

        HomeLoaded -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                IconButton(onClick = navigateToSettings) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = null
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = workoutTrackerDimens.gapLarge,
                        end = workoutTrackerDimens.gapLarge,
                    ),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Hi ${viewModel.getUserFirstName()}!",
                    style = workoutTrackerTypography.titleTextStyle,
                    modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                )
                if (exercises != null) {
                    Text(
                        text = "Your Estimated One Rep Maxes",
                        style = workoutTrackerTypography.medium18sp,
                    )
                    OneRepMaxCard(
                        maxList = viewModel.getMaxList(exercises!!),
                        modifier = Modifier.padding(
                            top = workoutTrackerDimens.gapMedium,
                            bottom = workoutTrackerDimens.gapNormal
                        )
                    )
                }
                if (workouts == null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WorkoutTrackerProgressIndicator()
                    }
                } else {
                    Text(
                        text = "Your Favorite Workouts",
                        style = workoutTrackerTypography.medium18sp,
                        modifier = Modifier.padding(bottom = workoutTrackerDimens.gapSmall)

                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = workoutTrackerDimens.gapNormal,
                                end = workoutTrackerDimens.gapNormal,
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (workouts!!.isEmpty()) {
                            item {
                                Text(
                                    text = stringResource(R.string.no_favorite_workouts),
                                    style = workoutTrackerTypography.medium18sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(top = workoutTrackerDimens.gapNormal)
                                )
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
                                            bottom = if (i == workouts!!.size - 1) workoutTrackerDimens.gapMedium + workoutTrackerDimens.bottomNavigationBarHeight else workoutTrackerDimens.gapSmall
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