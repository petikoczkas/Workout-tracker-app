package hu.bme.aut.workout_tracker.ui.screen.workout.workoutcomplete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton

@Composable
fun WorkoutCompleteScreen(
    focusManager: FocusManager,
    navigateToAddExercise: () -> Unit,
    navigateBack: () -> Unit,
) {
    focusManager.clearFocus()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.workout_complete),
                style = workoutTrackerTypography.titleTextStyle,
                modifier = Modifier
                    .padding(top = workoutTrackerDimens.gapVeryLarge)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            PrimaryButton(
                onClick = navigateToAddExercise,
                text = stringResource(R.string.add_exercise),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = workoutTrackerDimens.gapLarge)
            )
            PrimaryButton(
                onClick = navigateBack,
                text = stringResource(R.string.end_workout),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {}
    }
}