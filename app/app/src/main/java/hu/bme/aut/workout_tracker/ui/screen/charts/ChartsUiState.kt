package hu.bme.aut.workout_tracker.ui.screen.charts

import hu.bme.aut.workout_tracker.data.model.Exercise

sealed class ChartsUiState {
    data object ChartsInit : ChartsUiState()
    data class ChartsLoaded(
        val selectedExercise: Exercise,
        val selectedChart: String,
        val showDialog: Boolean
    ) : ChartsUiState()
}
