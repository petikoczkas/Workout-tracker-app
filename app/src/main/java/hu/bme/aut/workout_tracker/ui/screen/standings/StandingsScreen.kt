package hu.bme.aut.workout_tracker.ui.screen.standings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
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
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.card.UserCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen() {
    val listItems = arrayOf(
        "Chest",
        "Back",
        "Triceps"
    )
    val chestList = arrayOf(
        "Barbell bench press",
        "Dumbbell incline bench press",
        "Cable seated flys",
        "Machine incline chest press"
    )
    var subListItems = mutableListOf<String>()
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }
    var submenuExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapNormal),
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Standings")

        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.padding(bottom = workoutTrackerDimens.gapNormal),
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                label = { Text(text = "Label") },
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
                listItems.forEach { selectedOption ->
                    DropdownMenuItem(
                        text = {
                            Text(text = selectedOption)
                        },
                        onClick = {
                            subListItems = chestList.toMutableList()
                            submenuExpanded = true
                        }
                    )
                }
                DropdownMenu(
                    expanded = submenuExpanded,
                    onDismissRequest = { submenuExpanded = !submenuExpanded }
                ) {
                    subListItems.forEach { selectedOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = selectedOption)
                            },
                            onClick = {
                                selectedItem = selectedOption
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        UserCard()
    }
}