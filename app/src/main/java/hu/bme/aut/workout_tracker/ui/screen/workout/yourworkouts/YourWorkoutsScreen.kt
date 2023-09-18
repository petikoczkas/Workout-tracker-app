package hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.card.WorkoutCard

@Composable
fun YourWorkoutsScreen(
    onWorkoutClick: () -> Unit,
    onWorkoutEditClick: () -> Unit,
    onCreateWorkoutClick: () -> Unit
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
            Text("Your Workouts")
            WorkoutCard(
                name = "Name of the workout",
                exerciseNum = 8,
                onClick = onWorkoutClick,
                onEditClick = onWorkoutEditClick,
                onFavClick = { /*TODO*/ })
        }
        AddButton(
            onClick = onCreateWorkoutClick,
            text = "Create Workout",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp + workoutTrackerDimens.gapNormal)
        )
    }
}