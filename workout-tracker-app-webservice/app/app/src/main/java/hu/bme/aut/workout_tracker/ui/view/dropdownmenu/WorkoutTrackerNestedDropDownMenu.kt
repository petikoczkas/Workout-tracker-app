package hu.bme.aut.workout_tracker.ui.view.dropdownmenu

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.textfield.WorkoutTrackerTextField
import hu.bme.aut.workout_tracker.utils.AppData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTrackerNestedDropDownMenu(
    selectedItem: Exercise,
    onSelectedItemChange: (Exercise) -> Unit,
    exercises: List<Exercise>,
    modifier: Modifier = Modifier
) {
    var parentExpanded by remember {
        mutableStateOf(false)
    }
    var childExpanded by remember {
        mutableStateOf(false)
    }
    var selectedParent by remember {
        mutableStateOf(AppData.BODY_PARTS[0])
    }

    ExposedDropdownMenuBox(
        expanded = parentExpanded,
        onExpandedChange = { parentExpanded = !parentExpanded },
        modifier = modifier
    ) {
        WorkoutTrackerTextField(
            text = selectedItem.name,
            onTextChange = {},
            enabled = false,
            modifier = Modifier
                .menuAnchor(),
            placeholder = stringResource(R.string.select_an_exercise),
            textStyle = workoutTrackerTypography.medium16sp,
            trailingIcon = {
                TrailingIcon(
                    expanded = parentExpanded
                )
            },
        )
        DropdownMenu(
            expanded = parentExpanded,
            onDismissRequest = { parentExpanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            AppData.BODY_PARTS.forEach { selectedOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = selectedOption,
                            style = workoutTrackerTypography.medium14sp
                        )
                    },
                    onClick = {
                        selectedParent = selectedOption
                        childExpanded = true
                        parentExpanded = false
                    },
                )
            }
        }
        DropdownMenu(
            expanded = childExpanded,
            onDismissRequest = { childExpanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            if (exercises.none { it.category == selectedParent }) {
                Text(
                    text = stringResource(R.string.nesteddropdownmenu_empty_category_error_message),
                    style = workoutTrackerTypography.medium14sp,
                    modifier = Modifier.padding(MenuDefaults.DropdownMenuItemContentPadding)
                )
            } else {
                exercises.filter { it.category == selectedParent }.forEach { selectedOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectedOption.name,
                                style = workoutTrackerTypography.medium14sp
                            )
                        },
                        onClick = {
                            onSelectedItemChange(selectedOption)
                            childExpanded = false
                        },
                    )
                }
            }
        }
    }
}