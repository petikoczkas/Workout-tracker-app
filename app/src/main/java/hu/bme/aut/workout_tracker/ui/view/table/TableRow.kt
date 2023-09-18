package hu.bme.aut.workout_tracker.ui.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun TableRow(
    set: String,
    weight: String,
    onWeightChange: () -> Unit,
    reps: String,
    onRepsChange: () -> Unit,
) {
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
        TextTableCell(text = set, weight = 2f)
        TextFieldTableCell(text = weight, onValueChange = onWeightChange, weight = 3f)
        TextTableCell(text = "x", weight = 1f)
        TextFieldTableCell(text = reps, onValueChange = onRepsChange, weight = 2f)
    }
}