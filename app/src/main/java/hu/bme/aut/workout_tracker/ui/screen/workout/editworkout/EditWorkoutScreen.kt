package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun EditWorkoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapNormal),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Workout Name")
    }
}