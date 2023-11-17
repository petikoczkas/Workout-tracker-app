package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun WorkoutCard(
    name: String,
    exerciseNum: Int,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    isFav: Boolean,
    onFavClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var favorite by remember { mutableStateOf(isFav) }

    var isPressedFavorite by remember { mutableStateOf(false) }
    val scaleFavorite by animateFloatAsState(
        targetValue = if (isPressedFavorite) 0.6f else 1f,
        label = ""
    ) {
        if (isPressedFavorite) {
            isPressedFavorite = false
        }
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize))
            .height(workoutTrackerDimens.minWorkoutCardHeight)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(),
        shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(workoutTrackerDimens.minWorkoutCardHeight)
        ) {
            Column(
                modifier = Modifier
                    .height(workoutTrackerDimens.minWorkoutCardHeight)
                    .padding(workoutTrackerDimens.gapNormal)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = workoutTrackerTypography.medium20sp,
                    modifier = Modifier.padding(bottom = workoutTrackerDimens.gapSmall)
                )
                Text(
                    text = "$exerciseNum exercise",
                    style = workoutTrackerTypography.normal16sp
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                IconButton(
                    onClick = onEditClick,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        onFavClick()
                        favorite = !favorite
                        isPressedFavorite = true
                    },
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scaleFavorite
                            scaleY = scaleFavorite
                        },
                ) {
                    if (favorite) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite_full),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite_empty),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}