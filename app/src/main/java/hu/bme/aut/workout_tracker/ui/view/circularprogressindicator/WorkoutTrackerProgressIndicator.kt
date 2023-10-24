package hu.bme.aut.workout_tracker.ui.view.circularprogressindicator

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun WorkoutTrackerProgressIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentSize(align = Alignment.Center)
            .then(Modifier.size(workoutTrackerDimens.circularProgressIndicatorSize))
    )
}
