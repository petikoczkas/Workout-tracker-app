package hu.bme.aut.workout_tracker.ui.screen.registration

sealed class RegistrationUiState {

    data class RegistrationLoaded(
        val email: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        val passwordAgain: String
    ) : RegistrationUiState()

    data object RegistrationSuccess : RegistrationUiState()
}

