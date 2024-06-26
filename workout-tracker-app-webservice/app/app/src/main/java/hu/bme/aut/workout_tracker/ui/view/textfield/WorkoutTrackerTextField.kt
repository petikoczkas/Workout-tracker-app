package hu.bme.aut.workout_tracker.ui.view.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTrackerTextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = workoutTrackerTypography.normal16sp,
    placeholderTextStyle: TextStyle = workoutTrackerTypography.normal16sp,
    isError: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyBoardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        singleLine = true,
        isError = isError,
        enabled = enabled,
        placeholder = {
            Text(
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                style = placeholderTextStyle
            )
        },
        leadingIcon = leadingIcon,
        modifier = modifier
            .fillMaxWidth()
            .height(workoutTrackerDimens.workoutTrackerTextFieldHeight),
        colors = ExposedDropdownMenuDefaults.textFieldColors(
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBoardType,
            capitalization = KeyboardCapitalization.Words
        ),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(workoutTrackerDimens.textFieldCornerSize),
        trailingIcon = trailingIcon
    )
}