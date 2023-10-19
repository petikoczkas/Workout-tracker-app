package hu.bme.aut.workout_tracker.ui.view.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun AddButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(workoutTrackerDimens.minButtonHeight),
        shape = RoundedCornerShape(workoutTrackerDimens.primaryButtonCornerSize),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = null
        )
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontWeight = FontWeight.Medium),
            modifier = Modifier
                .weight(1f)
                .offset(x = (-12).dp)
        )
    }
}