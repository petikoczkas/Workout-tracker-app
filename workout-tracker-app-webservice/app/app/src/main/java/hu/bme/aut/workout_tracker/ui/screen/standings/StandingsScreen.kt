package hu.bme.aut.workout_tracker.ui.screen.standings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingInit
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.card.UserCard
import hu.bme.aut.workout_tracker.ui.view.circularprogressindicator.WorkoutTrackerProgressIndicator
import hu.bme.aut.workout_tracker.ui.view.dropdownmenu.WorkoutTrackerDropDownMenu

@Composable
fun StandingsScreen(
    viewModel: StandingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val exercises by viewModel.exercises.observeAsState()
    val users by viewModel.users.observeAsState()


    when (uiState) {
        StandingInit -> {
            viewModel.init()
        }

        is StandingLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.standings),
                    style = workoutTrackerTypography.titleTextStyle,
                    modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                )
                if (exercises.isNullOrEmpty() || users == null) {
                    WorkoutTrackerProgressIndicator()
                } else {
                    WorkoutTrackerDropDownMenu(
                        selectedItem = (uiState as StandingLoaded).selectedItem.name,
                        onSelectedItemChange = {
                            viewModel.onSelectedItemChangeByName(
                                name = it,
                                exercises = exercises!!
                            )
                        },
                        items = exercises!!.map { it.name },
                        modifier = Modifier.padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    if ((uiState as StandingLoaded).selectedItem.id != -1) {
                        LazyColumn(
                            modifier = Modifier.padding(bottom = workoutTrackerDimens.bottomNavigationBarHeight),
                        ) {
                            val bestUserMap = viewModel.getBestUsers(
                                users = users!!,
                                exerciseId = (uiState as StandingLoaded).selectedItem.id
                            )
                            if (bestUserMap.isEmpty()) {
                                item {
                                    Text(
                                        text = stringResource(R.string.standings_error_message),
                                        style = workoutTrackerTypography.medium18sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                itemsIndexed(bestUserMap.toList()) { i, data ->
                                    UserCard(
                                        place = i + 1,
                                        isCurrentUser = viewModel.isCurrentUser(data.first),
                                        name = "${data.first.firstName} ${data.first.lastName}",
                                        photo = data.first.photo,
                                        weight = data.second,
                                        modifier = Modifier
                                            .padding(
                                                top = workoutTrackerDimens.gapSmall,
                                                bottom = if (i == 49) workoutTrackerDimens.gapMedium else workoutTrackerDimens.gapSmall
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
}