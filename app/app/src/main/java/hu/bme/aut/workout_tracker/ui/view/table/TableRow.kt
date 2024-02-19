package hu.bme.aut.workout_tracker.ui.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.wear.compose.material.ContentAlpha
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun TableRow(
    set: String,
    weight: String,
    onWeightChange: (String) -> Unit,
    reps: String,
    onRepsChange: (String) -> Unit,
    enabled: Boolean,
    cleared: Boolean,
    keyboardActions: KeyboardActions,
    imeAction: ImeAction,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val alpha = if (enabled) 1f else ContentAlpha.disabled

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = workoutTrackerDimens.gapSmall)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(workoutTrackerDimens.tableRowCornerSize)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextTableCell(text = set, weight = 2f, modifier = Modifier.alpha(alpha))
        TextFieldTableCell(
            text = weight,
            onValueChange = onWeightChange,
            weight = 3f,
            enabled = enabled,
            cleared = cleared,
            keyboardActions = keyboardActions,
            imeAction = imeAction,
            modifier = modifier
        )
        TextTableCell(
            text = stringResource(R.string.x),
            weight = 1f,
            modifier = Modifier.alpha(alpha)
        )
        TextFieldTableCell(
            text = reps,
            onValueChange = onRepsChange,
            weight = 2f,
            enabled = enabled,
            cleared = cleared,
            keyboardActions = keyboardActions,
            imeAction = if (isLast) ImeAction.Done else imeAction
        )
    }
}