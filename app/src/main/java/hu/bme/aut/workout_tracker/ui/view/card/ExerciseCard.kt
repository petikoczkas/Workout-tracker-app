package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ExerciseCard(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    withIcon: Boolean = false,
    onRemoveClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize))
            .heightIn(workoutTrackerDimens.minUserCardHeight)
            .then(if (!withIcon) Modifier.clickable { onClick() } else Modifier),
        colors = CardDefaults.cardColors(),
        shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(workoutTrackerDimens.minUserCardHeight)
                .padding(
                    start = workoutTrackerDimens.gapNormal,
                    end = workoutTrackerDimens.gapMedium
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text)
            if (withIcon) {
                IconButton(onClick = onRemoveClick) {
                    Icon(painterResource(id = R.drawable.ic_remove), contentDescription = null)
                }
            }
        }
    }
}