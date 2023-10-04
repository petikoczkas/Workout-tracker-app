package hu.bme.aut.workout_tracker.ui.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
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
    keyboardActionsLast: KeyboardActions,
    modifier: Modifier = Modifier
) {
    val alpha = if (enabled) 1f else ContentAlpha.disabled
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = workoutTrackerDimens.gapSmall)
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
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
        TextTableCell(text = "x", weight = 1f, modifier = Modifier.alpha(alpha))
        TextFieldTableCell(
            text = reps,
            onValueChange = onRepsChange,
            weight = 2f,
            enabled = enabled,
            cleared = cleared,
            modifier = if (isLast) Modifier.onFocusChanged {
                if (it.isFocused) {
                    isFocused = true
                }
            } else Modifier,
            keyboardActions = if (isFocused) keyboardActionsLast else keyboardActions,
            imeAction = if (isFocused) ImeAction.Done else imeAction
        )
    }
}