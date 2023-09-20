package hu.bme.aut.workout_tracker.ui.screen.registration

import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationLoaded
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationSuccess
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton

@Composable
fun RegistrationScreen(
    navigateToRegistrationSuccess: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val registrationFailedEvent by viewModel.registrationFailedEvent.collectAsState()

    when (uiState) {
        is RegistrationLoaded -> {
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
                        value = (uiState as RegistrationLoaded).email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text(text = "Email") },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).firstName,
                        onValueChange = viewModel::onFirstNameChange,
                        label = { Text(text = "First Name") },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).lastName,
                        onValueChange = viewModel::onLastNameChange,
                        label = { Text(text = "Last Name") },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(text = "Password") },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).passwordAgain,
                        onValueChange = viewModel::onPasswordAgainChange,
                        label = { Text(text = "Password Again") },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                }
                PrimaryButton(
                    onClick = { viewModel.buttonOnClick() },
                    enabled = viewModel.isButtonEnabled(),
                    text = "Registrate",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
                if (registrationFailedEvent.isRegistrationFailed) {
                    Toast.makeText(
                        LocalContext.current,
                        registrationFailedEvent.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.handledRegistrationFailedEvent()
                }
            }
        }

        RegistrationSuccess -> {
            navigateToRegistrationSuccess()
        }
    }
}