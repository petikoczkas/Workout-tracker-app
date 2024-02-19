package hu.bme.aut.workout_tracker.ui.view.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun EditWorkoutTextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                style = workoutTrackerTypography.editWorkoutPlaceholderTextStyle
            )
        },
        textStyle = workoutTrackerTypography.editWorkoutTextStyle,
        colors = OutlinedTextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(workoutTrackerDimens.textFieldCornerSize),
    )
}