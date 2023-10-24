package hu.bme.aut.workout_tracker.ui.screen.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInInit
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInLoaded
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInSuccess
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.button.SecondaryButton
import hu.bme.aut.workout_tracker.ui.view.dialog.WorkoutTrackerAlertDialog

@Composable
fun SignInScreen(
    navigateToHome: () -> Unit,
    navigateToRegistration: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val signInFailedEvent by viewModel.signInFailedEvent.collectAsState()

    when (uiState) {

        is SignInLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapNormal),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(R.string.sign_in),
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    TextField(
                        value = (uiState as SignInLoaded).email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text(text = stringResource(R.string.email)) },
                        modifier = Modifier
                            .padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    TextField(
                        value = (uiState as SignInLoaded).password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(text = stringResource(R.string.password)) },
                        modifier = Modifier
                            .padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    SecondaryButton(
                        onClick = navigateToRegistration,
                        text = stringResource(R.string.registrate)
                    )
                }
                PrimaryButton(
                    onClick = { viewModel.buttonOnClick() },
                    enabled = viewModel.isButtonEnabled(),
                    text = stringResource(R.string.sign_in),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
                if (signInFailedEvent.isLoginFailed) {
                    WorkoutTrackerAlertDialog(
                        title = stringResource(R.string.login_failed),
                        description = stringResource(R.string.login_error_message),
                        onDismiss = { viewModel.handledSignInFailedEvent() }
                    )
                }
            }
        }

        SignInInit -> {
            if (viewModel.isLoggedIn()) navigateToHome()
        }

        SignInSuccess -> {
            navigateToHome()
        }
    }
}