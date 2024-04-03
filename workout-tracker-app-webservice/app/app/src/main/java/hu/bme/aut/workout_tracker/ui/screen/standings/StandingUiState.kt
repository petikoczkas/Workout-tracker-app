package hu.bme.aut.workout_tracker.ui.screen.standings

import hu.bme.aut.workout_tracker.data.model.Exercise


sealed class StandingUiState {
    data object StandingInit : StandingUiState()

    data class StandingLoaded(
        val selectedItem: Exercise,
    ) : StandingUiState()
}