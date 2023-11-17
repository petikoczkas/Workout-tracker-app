package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun FavWorkoutCard(
    name: String,
    exerciseNum: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize))
            .heightIn(workoutTrackerDimens.minFavWorkoutCardHeight)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(),
        shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize)
    ) {
        Column(
            modifier = Modifier
                .heightIn(workoutTrackerDimens.minFavWorkoutCardHeight)
                .padding(start = workoutTrackerDimens.gapNormal),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = name,
                style = workoutTrackerTypography.medium18sp,
                modifier = Modifier.padding(bottom = workoutTrackerDimens.gapTiny)
            )
            Text(
                text = "$exerciseNum exercise",
                style = workoutTrackerTypography.normal14sp
            )
        }
    }
}