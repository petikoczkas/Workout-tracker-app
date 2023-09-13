package hu.bme.aut.workout_tracker.ui.screen.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton

@Composable
fun RegistrationScreen(
    onRegistrateClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordAgain by rememberSaveable { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = workoutTrackerDimens.gapNormal)
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
                modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(
                    bottom = workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(text = "First Name") },
                modifier = Modifier.padding(
                    bottom = workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Last Name") },
                modifier = Modifier.padding(
                    bottom = workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.padding(
                    bottom = workoutTrackerDimens.gapLarge
                )
            )
            TextField(
                value = passwordAgain,
                onValueChange = { passwordAgain = it },
                label = { Text(text = "Password Again") },
                modifier = Modifier.padding(
                    bottom = workoutTrackerDimens.gapLarge
                )
            )
        }
        PrimaryButton(
            onClick = { onRegistrateClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = workoutTrackerDimens.gapNormal),
            text = "Registrate"
        )
    }
}