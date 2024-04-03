package hu.bme.aut.workout_tracker.ui.screen.signin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInInit
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInLoaded
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInSuccess
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.button.SecondaryButton
import hu.bme.aut.workout_tracker.ui.view.dialog.LoadingDialog
import hu.bme.aut.workout_tracker.ui.view.dialog.WorkoutTrackerAlertDialog
import hu.bme.aut.workout_tracker.ui.view.textfield.EmailTextField
import hu.bme.aut.workout_tracker.ui.view.textfield.PasswordTextField
import hu.bme.aut.workout_tracker.utils.dataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignInScreen(
    navigateToHome: () -> Unit,
    navigateToRegistration: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val signInFailedEvent by viewModel.signInFailedEvent.collectAsState()
    val showSavingDialog by viewModel.savingState.collectAsState()
    val context = LocalContext.current

    when (uiState) {

        is SignInLoaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapLarge),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(R.string.sign_in),
                        style = workoutTrackerTypography.titleTextStyle,
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    EmailTextField(
                        email = (uiState as SignInLoaded).email,
                        onEmailChange = viewModel::onEmailChange,
                        modifier = Modifier
                            .padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    PasswordTextField(
                        password = (uiState as SignInLoaded).password,
                        onPasswordChange = viewModel::onPasswordChange,
                        modifier = Modifier
                            .padding(bottom = workoutTrackerDimens.gapLarge)
                    )
                    SecondaryButton(
                        onClick = navigateToRegistration,
                        text = stringResource(R.string.registrate)
                    )
                }
                PrimaryButton(
                    onClick = { viewModel.buttonOnClick(context.dataStore) },
                    enabled = viewModel.isButtonEnabled(),
                    text = stringResource(R.string.sign_in),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
                if (signInFailedEvent.isLoginFailed) {
                    WorkoutTrackerAlertDialog(
                        title = stringResource(R.string.login_failed),
                        description =
                        if (signInFailedEvent.exception is IOException) stringResource(R.string.server_connection_error_message)
                        else stringResource(R.string.login_error_message),
                        onDismiss = { viewModel.handledSignInFailedEvent() }
                    )
                }
                if (showSavingDialog) {
                    LoadingDialog()
                }
            }
        }

        SignInInit -> {
            rememberCoroutineScope().launch {
                viewModel.savedUserEmail(context.dataStore)
                viewModel.savedToken(context.dataStore)
                delay(100)
                if (viewModel.isLoggedIn()) {
                    navigateToHome()
                } else {
                    viewModel.changeUiStateToSignInLoaded()
                }
                /*context.dataStore.edit {
                    it[TOKEN] = ""
                    it[USER_EMAIL] = ""
                }*/
            }
        }

        SignInSuccess -> {
            navigateToHome()
        }
    }
}
