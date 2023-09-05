package hu.bme.aut.workout_tracker.ui.screen.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun RegistrationScreen() {
    var email by rememberSaveable { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.workoutTrackerDimens.gapNormal)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Registration",
                modifier = Modifier.padding(vertical = MaterialTheme.workoutTrackerDimens.gapVeryLarge)
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(
                    bottom = MaterialTheme.workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "First Name") },
                modifier = Modifier.padding(
                    bottom = MaterialTheme.workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Last Name") },
                modifier = Modifier.padding(
                    bottom = MaterialTheme.workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Password") },
                modifier = Modifier.padding(
                    bottom = MaterialTheme.workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Password Again") },
                modifier = Modifier.padding(
                    bottom = MaterialTheme.workoutTrackerDimens.gapLarge
                )
            )
        }
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = MaterialTheme.workoutTrackerDimens.gapNormal
                ),
        ) {
            Text(text = "Registrate")
        }
    }
}