package hu.bme.aut.workout_tracker.ui.view.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun WorkoutTrackerTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyBoardType: KeyboardType = KeyboardType.Text,
) {
    val workoutTrackerTextFieldColors = OutlinedTextFieldDefaults.colors(
        /*backgroundColor = MaterialTheme.colors.secondary,
        textColor = MaterialTheme.colors.onBackground,
        leadingIconColor = MaterialTheme.colors.onSecondary,
        trailingIconColor = MaterialTheme.colors.onSecondary,
        unfocusedBorderColor = MaterialTheme.colors.onSecondary,
        focusedBorderColor = MaterialTheme.colors.primary,
        placeholderColor = MaterialTheme.colors.onSecondary,
        errorTrailingIconColor = MaterialTheme.colors.onSecondary,*/
        errorLeadingIconColor = MaterialTheme.colorScheme.error
    )

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        singleLine = true,
        enabled = enabled,
        isError = isError,
        label = { Text(label, style = workoutTrackerTypography.workoutTrackerTextFieldTextStyle) },
        leadingIcon = leadingIcon,
        modifier = modifier
            .fillMaxWidth(),
        //.height(MaterialTheme.beThereDimens.minBeThereTextFieldHeight),
        colors = workoutTrackerTextFieldColors,
        textStyle = workoutTrackerTypography.workoutTrackerTextFieldTextStyle,
        keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(workoutTrackerDimens.textFieldCornerSize),
        trailingIcon = trailingIcon
    )
}