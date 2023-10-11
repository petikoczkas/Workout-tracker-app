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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingInit
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.card.UserCard
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
            viewModel.getStandingExercises()
        }

        is StandingLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapNormal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Standings")
                if (exercises.isNullOrEmpty() || users == null) {
                    //TODO ProgressIndicator
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
                        modifier = Modifier.padding(workoutTrackerDimens.gapNormal)
                    )
                    if ((uiState as StandingLoaded).selectedItem.id.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.padding(bottom = 80.dp),
                        ) {
                            val bestUserList = viewModel.getBestUsers(
                                users!!,
                                (uiState as StandingLoaded).selectedItem.id
                            )
                            if (bestUserList.isEmpty()) {
                                item {
                                    Text(text = "There are no users who have completed the exercise.")
                                }
                            } else {
                                itemsIndexed(bestUserList.take(50)) { i, u ->
                                    UserCard(
                                        place = i + 1,
                                        name = u.name,
                                        photo = u.photo,
                                        weight = u.oneRepMaxCharts[(uiState as StandingLoaded).selectedItem.id]!!.max(),
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