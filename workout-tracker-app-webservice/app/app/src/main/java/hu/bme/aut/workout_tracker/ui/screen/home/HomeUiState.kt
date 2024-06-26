package hu.bme.aut.workout_tracker.ui.screen.home

sealed class HomeUiState {
    data object HomeInit : HomeUiState()

    data object HomeLoaded : HomeUiState()
}
