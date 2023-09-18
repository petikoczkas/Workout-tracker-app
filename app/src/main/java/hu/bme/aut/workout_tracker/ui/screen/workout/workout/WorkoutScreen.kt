package hu.bme.aut.workout_tracker.ui.screen.workout.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.button.SecondaryButton
import hu.bme.aut.workout_tracker.ui.view.table.TableRow
import hu.bme.aut.workout_tracker.ui.view.table.TextTableCell

@Composable
fun WorkoutScreen(
    onSaveClick: () -> Unit,
    onSwitchExerciseClick: () -> Unit
) {
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
            Text("Exercise Name")
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = workoutTrackerDimens.gapSmall),
                    ) {
                        TextTableCell(text = "Sets", weight = 2f)
                        TextTableCell(text = "Weight (kg)", weight = 3f)
                        TextTableCell(text = "", weight = 1f)
                        TextTableCell(text = "Reps", weight = 2f)
                    }
                }
                item {
                    TableRow(
                        set = "1.",
                        weight = "10",
                        onWeightChange = {},
                        reps = "10",
                        onRepsChange = {}
                    )
                    TableRow(
                        set = "2.",
                        weight = "100",
                        onWeightChange = {},
                        reps = "10",
                        onRepsChange = {}
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_remove),
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            SecondaryButton(
                onClick = onSwitchExerciseClick,
                text = "Switch Exercise"
            )
        }
        PrimaryButton(
            onClick = onSaveClick,
            text = "Save",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = workoutTrackerDimens.gapNormal)
        )
    }
}
