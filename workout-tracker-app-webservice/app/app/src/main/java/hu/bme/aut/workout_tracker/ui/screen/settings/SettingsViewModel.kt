package hu.bme.aut.workout_tracker.ui.screen.settings

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model_D.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsInit
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsLoaded
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsSaved
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
        _uiState.value = SettingsLoaded(name = "", imageUri = Uri.EMPTY)
        viewModelScope.launch {
            //currentUser = workoutTrackerPresenter.getCurrentUser()
            _uiState.value = SettingsLoaded(
                name = currentUser.name,
                imageUri = Uri.EMPTY
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { (_uiState.value as SettingsLoaded).copy(name = name) }
    }

    fun onImageChange(image: Uri) {
        _uiState.update { (_uiState.value as SettingsLoaded).copy(imageUri = image) }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun saveButtonOnClick(contentResolver: ContentResolver) {
        _savingState.value = true
        viewModelScope.launch {
            try {
                currentUser.name = (_uiState.value as SettingsLoaded).name
                if ((_uiState.value as SettingsLoaded).imageUri != Uri.EMPTY) {
                    workoutTrackerPresenter.uploadProfilePicture(
                        userId = currentUser.id,
                        imageBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(
                            contentResolver,
                            (_uiState.value as SettingsLoaded).imageUri
                        )),
                        onSuccess = {
                            currentUser.photo = it
                            viewModelScope.launch {
                                workoutTrackerPresenter.updateUser(currentUser)
                            }
                            _savingState.value = true
                            _uiState.value = SettingsSaved
                        }
                    )
                } else {
                    workoutTrackerPresenter.updateUser(currentUser)
                    _savingState.value = true
                    _uiState.value = SettingsSaved
                }
            } catch (e: Exception) {
                _savingState.value = false
                _updateUserFailedEvent.value =
                    UpdateUserFailure(isUpdateUserFailed = true, exception = e)
            }
        }
    }

    fun signOut() {
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