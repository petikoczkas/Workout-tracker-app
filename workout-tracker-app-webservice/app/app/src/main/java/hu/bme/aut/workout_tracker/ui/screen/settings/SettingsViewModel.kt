package hu.bme.aut.workout_tracker.ui.screen.settings

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsInit
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsLoaded
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsSaved
import hu.bme.aut.workout_tracker.utils.Constants
import hu.bme.aut.workout_tracker.utils.dataStore
import hu.bme.aut.workout_tracker.utils.getByteArray
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<SettingsUiState>(SettingsInit)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    var currentUser = User()

    private val _savingState = MutableStateFlow(false)
    val savingState = _savingState.asStateFlow()

    private val _updateUserFailedEvent =
        MutableStateFlow(UpdateUserFailure(isUpdateUserFailed = false, exception = null))
    val updateUserFailedEvent = _updateUserFailedEvent.asStateFlow()

    fun getCurrentUser() {
        _uiState.value = SettingsLoaded(firstName = "", lastName = "", imageUri = Uri.EMPTY)
        viewModelScope.launch {
            currentUser = workoutTrackerPresenter.getCurrentUser()
            _uiState.value = SettingsLoaded(
                firstName = currentUser.firstName,
                lastName = currentUser.lastName,
                imageUri = Uri.EMPTY
            )
        }
    }

    fun onFirstNameChange(name: String) {
        _uiState.update { (_uiState.value as SettingsLoaded).copy(firstName = name) }
    }

    fun onLastNameChange(name: String) {
        _uiState.update { (_uiState.value as SettingsLoaded).copy(lastName = name) }
    }

    fun onImageChange(image: Uri) {
        _uiState.update { (_uiState.value as SettingsLoaded).copy(imageUri = image) }
    }

    fun isSaveButtonEnabled(): Boolean {
        return (_uiState.value as SettingsLoaded).firstName.isNotEmpty() && (_uiState.value as SettingsLoaded).lastName.isNotEmpty()
    }

    @SuppressLint("Recycle")
    fun saveButtonOnClick(contentResolver: ContentResolver) {
        _savingState.value = true

        currentUser.firstName = (_uiState.value as SettingsLoaded).firstName
        currentUser.lastName = (_uiState.value as SettingsLoaded).lastName
        val imageUri = (_uiState.value as SettingsLoaded).imageUri
        if (imageUri != Uri.EMPTY) {
            val imageInputStream =
                contentResolver.openInputStream(imageUri)
            currentUser.photo =
                imageInputStream.getByteArray(width = 150, height = 200, quality = 75)
            imageInputStream?.close()
        }

        viewModelScope.launch {
            try {
                workoutTrackerPresenter.updateUser(
                    user = currentUser,
                    onSuccess = {
                        _uiState.value = SettingsSaved
                    }
                )

            } catch (e: Exception) {
                _savingState.value = false
                _updateUserFailedEvent.value =
                    UpdateUserFailure(isUpdateUserFailed = true, exception = e)
            }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            context.dataStore.edit {
                it[Constants.TOKEN] = ""
                it[Constants.USER_EMAIL] = ""
            }
        }
        workoutTrackerPresenter.signOut()
    }

    fun handledUpdateUserFailedEvent() {
        _updateUserFailedEvent.update { _updateUserFailedEvent.value.copy(isUpdateUserFailed = false) }
    }

    data class UpdateUserFailure(
        val isUpdateUserFailed: Boolean,
        val exception: Exception?
    )
}