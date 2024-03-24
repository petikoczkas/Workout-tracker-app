package hu.bme.aut.workout_tracker.ui.screen.settings

import android.net.Uri


sealed class SettingsUiState {
    data object SettingsInit : SettingsUiState()
    data class SettingsLoaded(
        val firstName: String,
        val lastName: String,
        val imageUri: Uri
    ) : SettingsUiState()

    data object SettingsSaved : SettingsUiState()
}
