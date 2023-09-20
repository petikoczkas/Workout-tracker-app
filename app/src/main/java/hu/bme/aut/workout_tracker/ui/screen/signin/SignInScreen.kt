package hu.bme.aut.workout_tracker.ui.screen.signin

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInInit
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInLoaded
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInSuccess
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.button.SecondaryButton

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
                        text = "Sign In",
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    TextField(
                        value = (uiState as SignInLoaded).email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text(text = "Email") },
                        modifier = Modifier
                            .padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    TextField(
                        value = (uiState as SignInLoaded).password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(text = "Password") },
                        modifier = Modifier
                            .padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    SecondaryButton(
                        onClick = navigateToRegistration,
                        text = "Registrate"
                    )
                }
                PrimaryButton(
                    onClick = { viewModel.buttonOnClick() },
                    enabled = viewModel.isButtonEnabled(),
                    text = "Sign In",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
                if (signInFailedEvent.isLoginFailed) {
                    Toast.makeText(
                        LocalContext.current,
                        signInFailedEvent.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.handledSignInFailedEvent()
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