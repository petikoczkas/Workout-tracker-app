package hu.bme.aut.workout_tracker.ui.screen.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsInit
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.chart.WorkoutTrackerChart
import hu.bme.aut.workout_tracker.ui.view.dropdownmenu.WorkoutTrackerNestedDropDownMenu
import hu.bme.aut.workout_tracker.ui.view.toggle.TriStateToggle

@Composable
fun ChartsScreen(
    viewModel: ChartsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val exercises by viewModel.exercises.observeAsState()

    when (uiState) {
        ChartsInit -> {
            viewModel.getExercises()
        }

        is ChartsLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapNormal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Charts")
                if (exercises == null) {
                    //TODO ProgressIndicator
                } else {
                    WorkoutTrackerNestedDropDownMenu(
                        selectedItem = (uiState as ChartsLoaded).selectedExercise,
                        onSelectedItemChange = viewModel::onSelectedExerciseChange,
                        exercises = viewModel.getUserExercises(exercises!!),
                        modifier = Modifier.padding(workoutTrackerDimens.gapNormal)
                    )
                    TriStateToggle(
                        selectedOption = (uiState as ChartsLoaded).selectedChart,
                        onSelectionChange = viewModel::onSelectedChartChange,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if ((uiState as ChartsLoaded).selectedExercise.id.isNotEmpty()) {
                            val data = viewModel.getSelectedChart(
                                id = (uiState as ChartsLoaded).selectedExercise.id,
                                chartName = (uiState as ChartsLoaded).selectedChart
                            )
                            if (data.entries[0].size < 2) {
                                Text(
                                    text = "There is not enough data to display on the chart.",
                                    modifier = Modifier.padding(horizontal = workoutTrackerDimens.gapSmall),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                WorkoutTrackerChart(chartEntryModel = data)
                            }
                        }
                    }
                }
            }
        }
    }
}