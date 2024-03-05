package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

sealed class EditWorkoutUiState {
    data object EditWorkoutInit : EditWorkoutUiState()

    data class EditWorkoutLoaded(
        val name: String
    ) : EditWorkoutUiState()

    data object EditWorkoutSaved : EditWorkoutUiState()
}
