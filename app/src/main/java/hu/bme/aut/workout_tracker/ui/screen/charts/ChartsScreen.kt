package hu.bme.aut.workout_tracker.ui.screen.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun ChartsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.workoutTrackerDimens.gapNormal),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Charts")
    }
}