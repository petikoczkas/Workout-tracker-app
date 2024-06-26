package hu.bme.aut.workout_tracker.ui.view.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun ChartsInformationDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismissRequest(false) }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onDismissRequest(false) },
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor,
                tonalElevation = workoutTrackerDimens.addExerciseDialogTonalElevation
            ) {
                Column(modifier = Modifier.padding(workoutTrackerDimens.gapNormal)) {
                    Text(
                        text = stringResource(R.string.volume_chart),
                        style = workoutTrackerTypography.medium20sp,
                        modifier = Modifier.padding(bottom = workoutTrackerDimens.gapSmall)
                    )
                    Text(
                        text = stringResource(R.string.volume_chart_description),
                        style = workoutTrackerTypography.normal16sp
                    )
                    Text(
                        text = stringResource(R.string.volume_chart_formula),
                        style = workoutTrackerTypography.normal16sp,
                        modifier = Modifier.padding(top = workoutTrackerDimens.gapTiny)
                    )
                    Text(
                        text = stringResource(R.string.average_one_rep_max_chart),
                        style = workoutTrackerTypography.medium20sp,
                        modifier = Modifier.padding(
                            top = workoutTrackerDimens.gapNormal,
                            bottom = workoutTrackerDimens.gapSmall
                        )
                    )
                    Text(
                        text = stringResource(R.string.average_one_rep_max_chart_description),
                        style = workoutTrackerTypography.normal16sp
                    )
                    Text(
                        text = stringResource(R.string.average_one_rep_max_chart_formula),
                        style = workoutTrackerTypography.normal16sp,
                        modifier = Modifier.padding(top = workoutTrackerDimens.gapTiny)
                    )
                    Text(
                        text = stringResource(R.string.one_rep_max_chart),
                        style = workoutTrackerTypography.medium20sp,
                        modifier = Modifier.padding(
                            top = workoutTrackerDimens.gapNormal,
                            bottom = workoutTrackerDimens.gapSmall
                        )
                    )
                    Text(
                        text = stringResource(R.string.one_rep_max_chart_description),
                        style = workoutTrackerTypography.normal16sp
                    )
                    Text(
                        text = stringResource(R.string.one_rep_max_chart_formula),
                        style = workoutTrackerTypography.normal16sp,
                        modifier = Modifier.padding(top = workoutTrackerDimens.gapTiny)
                    )
                }
            }
        }
    }
}