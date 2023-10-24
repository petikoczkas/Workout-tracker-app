package hu.bme.aut.workout_tracker.ui.screen.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInInit
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInLoaded
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInSuccess
import hu.bme.aut.workout_tracker.utils.isValidEmail
import hu.bme.aut.workout_tracker.utils.removeEmptyLines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInInit)
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _signInFailedEvent =
        MutableStateFlow(SignInFailure(isLoginFailed = false))
    val signInFailedEvent = _signInFailedEvent.asStateFlow()

    fun onEmailChange(emailAddress: String) {
        _uiState.update { (_uiState.value as SignInLoaded).copy(email = emailAddress.removeEmptyLines()) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { (_uiState.value as SignInLoaded).copy(password = password) }
    }

    fun isButtonEnabled(): Boolean {
        if (!(_uiState.value as SignInLoaded).email.isValidEmail() or (_uiState.value as SignInLoaded).password.isBlank()) return false
        return true
    }

    fun isLoggedIn(): Boolean {
        if (workoutTrackerPresenter.isLoggedIn()) return true
        _uiState.value = SignInLoaded("", "")
        return false
    }

    fun buttonOnClick() {
        val email = (_uiState.value as SignInLoaded).email
        val password = (_uiState.value as SignInLoaded).password
        viewModelScope.launch {
            try {
                workoutTrackerPresenter.signIn(
                    email = email,
                    password = password,
                    onSuccess = {
                        _uiState.value = SignInSuccess
                    },
                    onFailure = {
                        _signInFailedEvent.value =
                            SignInFailure(isLoginFailed = true)
                    }
                )
            } catch (e: Exception) {
                _signInFailedEvent.value = SignInFailure(isLoginFailed = true)
            }
        }
    }

    fun handledSignInFailedEvent() {
        _uiState.update { (_uiState.value as SignInLoaded).copy(email = "", password = "") }
        _signInFailedEvent.update { _signInFailedEvent.value.copy(isLoginFailed = false) }
    }

    data class SignInFailure(
        val isLoginFailed: Boolean
    )
}