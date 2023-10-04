package hu.bme.aut.workout_tracker.ui.view.dropdownmenu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hu.bme.aut.workout_tracker.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTrackerDropDownMenu(
    selectedItem: String,
    onSelectedItemChange: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(),
            label = { Text(text = "Select a body part") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Constants.BODY_PARTS.forEach { selectedOption ->
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