package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun UserCard(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize))
            .heightIn(workoutTrackerDimens.minUserCardHeight),
        colors = CardDefaults.cardColors(),
        shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(workoutTrackerDimens.minUserCardHeight)
                .padding(workoutTrackerDimens.gapMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1.",
                    modifier = Modifier.padding(horizontal = workoutTrackerDimens.gapMedium)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Text(
                text = "Name of the user",
                modifier = Modifier
                    .padding(horizontal = workoutTrackerDimens.gapMedium)
                    .weight(5f)
            )
            Text(
                text = "100 kg",
                modifier = Modifier.weight(2f)
            )
        }
    }
}