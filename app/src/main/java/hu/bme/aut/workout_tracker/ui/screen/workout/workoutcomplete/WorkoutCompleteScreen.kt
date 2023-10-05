package hu.bme.aut.workout_tracker.ui.screen.workout.workoutcomplete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WorkoutCompleteScreen(
    navigateToAddExercise: () -> Unit,
    navigateBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    keyboardController?.hide()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapNormal),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Workout Complete")
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            PrimaryButton(
                onClick = navigateToAddExercise,
                text = "Add Exercise",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = workoutTrackerDimens.gapNormal)
            )
            PrimaryButton(
                onClick = navigateBack,
                text = "End Workout",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}