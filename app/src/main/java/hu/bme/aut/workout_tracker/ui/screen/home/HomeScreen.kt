package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.card.FavWorkoutCard

@Composable
fun HomeScreen(
    onWorkoutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapNormal),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { onSettingsClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null
                )
            }
        }
        Text("Home")
        FavWorkoutCard(
            name = "Name of the workout",
            exerciseNum = 8,
            onClick = onWorkoutClick,
            modifier = Modifier.padding(workoutTrackerDimens.gapNormal)
        )
    }
}