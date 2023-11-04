package hu.bme.aut.workout_tracker.ui.screen.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationLoaded
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationSuccess
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.checker.PasswordChecker
import hu.bme.aut.workout_tracker.ui.view.dialog.WorkoutTrackerAlertDialog
import hu.bme.aut.workout_tracker.ui.view.textfield.EmailTextField
import hu.bme.aut.workout_tracker.ui.view.textfield.PasswordTextField
import hu.bme.aut.workout_tracker.ui.view.textfield.WorkoutTrackerTextField

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
                    .padding(horizontal = workoutTrackerDimens.gapLarge)
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
                        style = workoutTrackerTypography.titleTextStyle,
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    EmailTextField(
                        email = (uiState as RegistrationLoaded).email,
                        onEmailChange = viewModel::onEmailChange,
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    WorkoutTrackerTextField(
                        text = (uiState as RegistrationLoaded).firstName,
                        onTextChange = viewModel::onFirstNameChange,
                        label = stringResource(R.string.first_name),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    WorkoutTrackerTextField(
                        text = (uiState as RegistrationLoaded).lastName,
                        onTextChange = viewModel::onLastNameChange,
                        label = stringResource(R.string.last_name),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapLarge
                        )
                    )
                    PasswordTextField(
                        password = (uiState as RegistrationLoaded).password,
                        onPasswordChange = viewModel::onPasswordChange,
                        modifier = Modifier.padding(
                            bottom = workoutTrackerDimens.gapMedium
                        )
                    )
                    PasswordChecker(
                        password = (uiState as RegistrationLoaded).password,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = workoutTrackerDimens.gapLarge)
                    )
                    PasswordTextField(
                        password = (uiState as RegistrationLoaded).passwordAgain,
                        onPasswordChange = viewModel::onPasswordAgainChange,
                        isPasswordAgain = true,
                        firstPassword = (uiState as RegistrationLoaded).password,
                        modifier = Modifier.padding(
                            top = workoutTrackerDimens.gapMedium,
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
                        description = stringResource(R.string.registration_error_message),
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