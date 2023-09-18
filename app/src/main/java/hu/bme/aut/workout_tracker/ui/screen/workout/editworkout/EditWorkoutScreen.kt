package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.AddButton
import hu.bme.aut.workout_tracker.ui.view.card.ExerciseCard

@Composable
fun EditWorkoutScreen(
    onAddExerciseClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapNormal),
    ) {
        Text("Workout Name")
        ExerciseCard(
            text = "Barbell Bench Press"
        )
        AddButton(
            onClick = onAddExerciseClick,
            text = "Add Exercise",
            modifier = Modifier.fillMaxWidth()
        )
    }
}