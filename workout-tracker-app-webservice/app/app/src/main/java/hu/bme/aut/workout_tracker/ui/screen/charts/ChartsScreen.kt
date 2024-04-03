package hu.bme.aut.workout_tracker.ui.screen.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsInit
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsLoaded
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.chart.WorkoutTrackerChart
import hu.bme.aut.workout_tracker.ui.view.circularprogressindicator.WorkoutTrackerProgressIndicator
import hu.bme.aut.workout_tracker.ui.view.dialog.ChartsInformationDialog
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = { viewModel.onShowDialogChange(true) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = null
                    )
                }
                ChartsInformationDialog(
                    showDialog = (uiState as ChartsLoaded).showDialog,
                    onDismissRequest = viewModel::onShowDialogChange,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.charts),
                    style = workoutTrackerTypography.titleTextStyle,
                    modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                )
                if (exercises == null) {
                    WorkoutTrackerProgressIndicator()
                } else {
                    WorkoutTrackerNestedDropDownMenu(
                        selectedItem = (uiState as ChartsLoaded).selectedExercise,
                        onSelectedItemChange = viewModel::onSelectedExerciseChange,
                        exercises = viewModel.getUserExercises(exercises!!),
                        modifier = Modifier.padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    TriStateToggle(
                        selectedOption = (uiState as ChartsLoaded).selectedChart,
                        onSelectionChange = viewModel::onSelectedChartChange,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = workoutTrackerDimens.bottomNavigationBarHeight),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if ((uiState as ChartsLoaded).selectedExercise.id != -1) {
                            val data = viewModel.getSelectedChart(
                                id = (uiState as ChartsLoaded).selectedExercise.id,
                                chartName = (uiState as ChartsLoaded).selectedChart
                            )
                            if (data.entries[0].size < 2) {
                                Text(
                                    text = stringResource(R.string.chart_error_message),
                                    style = workoutTrackerTypography.medium18sp,
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