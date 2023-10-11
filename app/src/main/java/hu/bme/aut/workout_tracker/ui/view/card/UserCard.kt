package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun UserCard(
    place: Int,
    name: String,
    photo: String,
    weight: Double,
    modifier: Modifier = Modifier
) {
    val weightNumber =
        if (weight.toInt().toDouble() == weight) {
            weight.toInt()
        } else {
            weight
        }

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
                    .weight(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$place.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = workoutTrackerDimens.gapSmall)

                )
                Box(
                    modifier = Modifier.weight(3f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (photo.isEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(model = photo),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            Text(
                text = name,
                modifier = Modifier
                    .weight(8f)
                    .padding(start = workoutTrackerDimens.gapMedium)
            )
            Text(
                text = "$weightNumber kg",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(4f)
                    .padding(workoutTrackerDimens.gapMedium)
            )
        }
    }
}