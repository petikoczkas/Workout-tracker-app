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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationLoaded
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationSuccess
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.dialog.WorkoutTrackerAlertDialog

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
                        text = stringResource(R.string.registration),
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text(text = stringResource(R.string.email)) },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).firstName,
                        onValueChange = viewModel::onFirstNameChange,
                        label = { Text(text = stringResource(R.string.first_name)) },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).lastName,
                        onValueChange = viewModel::onLastNameChange,
                        label = { Text(text = stringResource(R.string.last_name)) },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(text = stringResource(R.string.password)) },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    TextField(
                        value = (uiState as RegistrationLoaded).passwordAgain,
                        onValueChange = viewModel::onPasswordAgainChange,
                        label = { Text(text = stringResource(R.string.password_again)) },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                }
                PrimaryButton(
                    onClick = { viewModel.buttonOnClick() },
                    enabled = viewModel.isButtonEnabled(),
                    text = stringResource(R.string.registrate),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
                if (registrationFailedEvent.isRegistrationFailed) {
                    WorkoutTrackerAlertDialog(
                        title = stringResource(R.string.registration_failed),
                        description = registrationFailedEvent.exception?.message.toString(),
                        onDismiss = { viewModel.handledRegistrationFailedEvent() }
                    )
                }
            }
        }

        RegistrationSuccess -> {
            navigateToRegistrationSuccess()
        }
    }
}