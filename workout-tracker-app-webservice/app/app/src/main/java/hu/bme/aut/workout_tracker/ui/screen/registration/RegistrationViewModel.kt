package hu.bme.aut.workout_tracker.ui.screen.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model_D.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationLoaded
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationUiState.RegistrationSuccess
import hu.bme.aut.workout_tracker.utils.isValidEmail
import hu.bme.aut.workout_tracker.utils.isValidPassword
import hu.bme.aut.workout_tracker.utils.passwordMatches
import hu.bme.aut.workout_tracker.utils.removeEmptyLines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegistrationUiState>(
        RegistrationLoaded(
            email = "",
            firstName = "",
            lastName = "",
            password = "",
            passwordAgain = "",
        )
    )
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _savingState = MutableStateFlow(false)
    val savingState = _savingState.asStateFlow()

    private val _registrationFailedEvent =
        MutableStateFlow(RegistrationFailure(isRegistrationFailed = false))
    val registrationFailedEvent = _registrationFailedEvent.asStateFlow()

    fun onEmailChange(emailAddress: String) {
        _uiState.update { (_uiState.value as RegistrationLoaded).copy(email = emailAddress.removeEmptyLines()) }
    }

    fun onFirstNameChange(firstName: String) {
        _uiState.update { (_uiState.value as RegistrationLoaded).copy(firstName = firstName) }
    }

    fun onLastNameChange(lastName: String) {
        _uiState.update { (_uiState.value as RegistrationLoaded).copy(lastName = lastName) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { (_uiState.value as RegistrationLoaded).copy(password = password) }
    }

    fun onPasswordAgainChange(passwordAgain: String) {
        _uiState.update { (_uiState.value as RegistrationLoaded).copy(passwordAgain = passwordAgain) }
    }

    fun isButtonEnabled(): Boolean {
        if (!(_uiState.value as RegistrationLoaded).email.isValidEmail()
            or (_uiState.value as RegistrationLoaded).firstName.isBlank()
            or (_uiState.value as RegistrationLoaded).lastName.isBlank()
            or !(_uiState.value as RegistrationLoaded).password.isValidPassword()
            or !(_uiState.value as RegistrationLoaded).passwordAgain.isValidPassword()
            or !(_uiState.value as RegistrationLoaded).password.passwordMatches((_uiState.value as RegistrationLoaded).passwordAgain)
        ) return false
        return true
    }

    fun buttonOnClick() {
        val email = (_uiState.value as RegistrationLoaded).email
        val password = (_uiState.value as RegistrationLoaded).password
        _savingState.value = true
        viewModelScope.launch {
            try {
                workoutTrackerPresenter.registrate(
                    email = email,
                    password = password,
                    user = User(
                        name = "${(_uiState.value as RegistrationLoaded).firstName} ${(_uiState.value as RegistrationLoaded).lastName}"
                    ),
                    onSuccess = {
                        _uiState.value = RegistrationSuccess
                        _savingState.value = true
                    },
                    onFailure = {
                        _registrationFailedEvent.value =
                            RegistrationFailure(isRegistrationFailed = true)
                    }
                )
            } catch (e: Exception) {
                _savingState.value = false
                _registrationFailedEvent.value = RegistrationFailure(
                    isRegistrationFailed = true
                )
            }
        }
    }

    fun handledRegistrationFailedEvent() {
        _uiState.update {
            (_uiState.value as RegistrationLoaded).copy(
                email = "",
                password = "",
                passwordAgain = "",
            )
        }
        _registrationFailedEvent.update { _registrationFailedEvent.value.copy(isRegistrationFailed = false) }
    }

    data class RegistrationFailure(
        val isRegistrationFailed: Boolean
    )
}