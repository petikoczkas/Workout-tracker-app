package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerColors
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun UserCard(
    place: Int,
    isCurrentUser: Boolean,
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
    Card(
        shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize),
        border = if (isCurrentUser) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(workoutTrackerDimens.minUserCardHeight),
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onBackground)
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
                    .weight(4f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (place > 3) {
                    Text(
                        text = "$place.",
                        textAlign = TextAlign.Center,
                        style = workoutTrackerTypography.medium16sp,
                        modifier = Modifier
                            .weight(2f)
                            .padding(end = workoutTrackerDimens.gapSmall)

                    )
                } else {
                    val color = when (place) {
                        1 -> {
                            workoutTrackerColors.gold
                        }

                        2 -> {
                            workoutTrackerColors.silver
                        }

                        else -> {
                            workoutTrackerColors.bronze
                        }
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trophy),
                        tint = color,
                        modifier = Modifier
                            .weight(2f)
                            .padding(end = workoutTrackerDimens.gapSmall),
                        contentDescription = null
                    )
                }
                Box(
                    modifier = Modifier.weight(3f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(workoutTrackerDimens.userCardImageSize)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        if (photo.isEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier.size(workoutTrackerDimens.userCardIconSize)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(model = photo),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(workoutTrackerDimens.userCardImageSize)
                            )
                        }
                    }
                }
            }
            Text(
                text = name,
                style = workoutTrackerTypography.medium16sp,
                modifier = Modifier
                    .weight(8f)
                    .padding(start = workoutTrackerDimens.gapMedium)
            )
            Text(
                text = "$weightNumber kg",
                style = workoutTrackerTypography.medium16sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(5f)
                    .padding(workoutTrackerDimens.gapMedium)
            )
        }
    }
}