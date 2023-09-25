package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

sealed class AddExerciseUiState {

    data object AddExerciseInit : AddExerciseUiState()

    data class AddExerciseLoaded(
        val selectedItem: String,
        val showDialog: Boolean,
        val newExercise: String
    ) : AddExerciseUiState()

    data object AddExerciseSaved : AddExerciseUiState()
}