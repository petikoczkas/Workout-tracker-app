package hu.bme.aut.workout_tracker.ui.view.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun OneRepMaxCard(
    maxList: List<List<String>>,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize),
        shadowElevation = workoutTrackerDimens.triStateToggleShadowElevation,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(workoutTrackerDimens.workoutCardCornerSize))
        ) {
            maxList.forEach { l ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(
                            vertical = workoutTrackerDimens.gapMedium,
                            horizontal = workoutTrackerDimens.gapMedium,
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = l[0],
                        style = workoutTrackerTypography.bold16sp,
                        maxLines = 1
                    )
                    Text(
                        text = if (l[1] == "-") l[1] else "${l[1]} kg",
                        style = workoutTrackerTypography.bold32sp,
                    )
                }
            }
        }
    }
}