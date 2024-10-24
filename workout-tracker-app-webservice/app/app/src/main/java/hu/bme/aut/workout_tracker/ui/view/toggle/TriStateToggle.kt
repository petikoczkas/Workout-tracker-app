package hu.bme.aut.workout_tracker.ui.view.toggle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.utils.AppData.chartsList

@Composable
fun TriStateToggle(
    selectedOption: String,
    onSelectionChange: (String) -> Unit
) {
    val backgroundColor =
        MaterialTheme.colorScheme.surfaceColorAtElevation(workoutTrackerDimens.triStateToggleBackgroundColorElevation)

    Surface(
        shape = RoundedCornerShape(workoutTrackerDimens.triStateToggleCornerSize),
        shadowElevation = workoutTrackerDimens.triStateToggleShadowElevation,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .testTag("TriStateToggle")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(workoutTrackerDimens.triStateToggleCornerSize))
                .background(backgroundColor),
        ) {
            chartsList.forEach { text ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(workoutTrackerDimens.triStateToggleCornerSize))
                        .clickable {
                            onSelectionChange(text)
                        }
                        .background(
                            if (text == selectedOption) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                backgroundColor
                            }
                        )
                        .padding(
                            vertical = workoutTrackerDimens.gapMedium,
                            horizontal = workoutTrackerDimens.gapNormal,
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = text,
                        style = workoutTrackerTypography.triStateToggleTextStyle,
                        maxLines = 2,
                    )
                }
            }
        }
    }
}