package hu.bme.aut.workout_tracker.ui.view.dropdownmenu

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.textfield.WorkoutTrackerTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTrackerDropDownMenu(
    selectedItem: String,
    onSelectedItemChange: (String) -> Unit,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        WorkoutTrackerTextField(
            text = selectedItem,
            onTextChange = {},
            enabled = false,
            modifier = Modifier
                .menuAnchor(),
            placeholder = stringResource(R.string.select_a_body_part),
            textStyle = workoutTrackerTypography.medium16sp,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.exposedDropdownSize(),
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { selectedOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectedOption)
                    },
                    onClick = {
                        onSelectedItemChange(selectedOption)
                        expanded = false
                    }
                )
            }
        }
    }
}