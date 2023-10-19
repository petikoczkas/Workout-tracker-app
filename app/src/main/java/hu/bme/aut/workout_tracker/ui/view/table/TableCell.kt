package hu.bme.aut.workout_tracker.ui.view.table

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.ContentAlpha
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun RowScope.TextTableCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier
) {

    Text(
        text = text,
        modifier = modifier
            .weight(weight)
            .padding(workoutTrackerDimens.gapMedium),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.TextFieldTableCell(
    text: String,
    onValueChange: (String) -> Unit,
    weight: Float,
    enabled: Boolean,
    cleared: Boolean,
    keyboardActions: KeyboardActions,
    imeAction: ImeAction,
    modifier: Modifier = Modifier
) {
    val alpha = if (enabled) 1f else ContentAlpha.disabled

    val textFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = TextRange(text.length)
            )
        )
    }

    if (cleared) textFieldValue.value = TextFieldValue(
        text = "",
    )
    else textFieldValue.value = TextFieldValue(
        text = text,
        selection = TextRange(text.length)
    )

    BasicTextField(
        value = textFieldValue.value,
        onValueChange = {
            textFieldValue.value = TextFieldValue(
                text = it.text,
                selection = TextRange(it.text.length)
            )
            onValueChange(it.text)
        },
        enabled = enabled,
        modifier = modifier
            .height(workoutTrackerDimens.tableCellHeight)
            .weight(weight)
            .alpha(alpha)
            .padding(workoutTrackerDimens.gapMedium),
        textStyle = LocalTextStyle.current.copy(
            color = Color.White,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Gray,
                unfocusedContainerColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
            ),
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                top = workoutTrackerDimens.gapNone,
                bottom = workoutTrackerDimens.gapNone,
            ),
        )
    }
}