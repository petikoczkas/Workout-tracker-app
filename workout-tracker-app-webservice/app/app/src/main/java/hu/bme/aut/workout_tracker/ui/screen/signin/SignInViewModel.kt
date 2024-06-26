package hu.bme.aut.workout_tracker.ui.screen.signin

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthLogin
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInInit
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInLoaded
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState.SignInSuccess
import hu.bme.aut.workout_tracker.utils.Constants.TOKEN
import hu.bme.aut.workout_tracker.utils.Constants.USER_EMAIL
import hu.bme.aut.workout_tracker.utils.Constants.currentUserEmail
import hu.bme.aut.workout_tracker.utils.Constants.token
import hu.bme.aut.workout_tracker.utils.isValidEmail
import hu.bme.aut.workout_tracker.utils.removeEmptyLines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInInit)
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _savingState = MutableStateFlow(false)
    val savingState = _savingState.asStateFlow()

    private val _signInFailedEvent =
        MutableStateFlow(SignInFailure(isLoginFailed = false, exception = null))
    val signInFailedEvent = _signInFailedEvent.asStateFlow()

    fun onEmailChange(emailAddress: String) {
        _uiState.update {
            (_uiState.value as SignInLoaded).copy(
                email = emailAddress.removeEmptyLines().lowercase()
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { (_uiState.value as SignInLoaded).copy(password = password) }
    }

    fun isButtonEnabled(): Boolean {
        return !(!(_uiState.value as SignInLoaded).email.isValidEmail() or (_uiState.value as SignInLoaded).password.isBlank())
    }

    fun savedUserEmail(dataStore: DataStore<Preferences>) {
        viewModelScope.launch {
            dataStore.data
                .map { preferences ->
                    preferences[USER_EMAIL] ?: ""
                }.collect {
                    currentUserEmail = it
                }
        }
    }

    fun savedToken(dataStore: DataStore<Preferences>) {
        viewModelScope.launch {
            dataStore.data
                .map { preferences ->
                    preferences[TOKEN] ?: ""
                }.collect {
                    if (token.isEmpty()) {
                        token = it
                    }
                }
        }
    }

    fun isLoggedIn(): Boolean {
        return token.isNotEmpty() && currentUserEmail.isNotEmpty()
    }

    fun changeUiStateToSignInLoaded() {
        _uiState.value = SignInLoaded("", "")
    }

    fun buttonOnClick(dataStore: DataStore<Preferences>) {
        val email = (_uiState.value as SignInLoaded).email
        val password = (_uiState.value as SignInLoaded).password
        _savingState.value = true
        workoutTrackerPresenter.signIn(
            userAuthLogin = UserAuthLogin(email = email, password = password),
            onSuccess = { token ->
                viewModelScope.launch {
                    dataStore.edit {
                        it[TOKEN] = "Bearer $token"
                        it[USER_EMAIL] = email
                    }
                }
                _uiState.value = SignInSuccess
            },
            onFailure = {
                _signInFailedEvent.value =
                    SignInFailure(isLoginFailed = true, exception = it)
                _savingState.value = false
            }
        )
    }

    fun handledSignInFailedEvent() {
        _uiState.update { (_uiState.value as SignInLoaded).copy(email = "", password = "") }
        _signInFailedEvent.update { _signInFailedEvent.value.copy(isLoginFailed = false) }
    }

    data class SignInFailure(
        val isLoginFailed: Boolean,
        val exception: Exception?
    )
}