package hu.bme.aut.workout_tracker.ui.screen.signin


sealed class SignInUiState {

    data object SignInInit : SignInUiState()
    data class SignInLoaded(
        val email: String,
        val password: String
    ) : SignInUiState()

    data object SignInSuccess : SignInUiState()
}
