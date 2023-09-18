package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun WorkoutCard(
    name: String,
    exerciseNum: Int,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onFavClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize))
            .heightIn(workoutTrackerDimens.minWorkoutCardHeight)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(),
        shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(workoutTrackerDimens.minWorkoutCardHeight)
                .padding(workoutTrackerDimens.gapMedium)
        ) {
            Column(
                modifier = Modifier
                    .height(workoutTrackerDimens.minWorkoutCardHeight)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    modifier = Modifier.padding(bottom = workoutTrackerDimens.gapSmall)
                )
                Text(text = "$exerciseNum exercise")
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = onFavClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star_empty),
                        contentDescription = null
                    )
                }
                //Box(modifier = Modifier.height(40.dp))
            }
        }
    }
}