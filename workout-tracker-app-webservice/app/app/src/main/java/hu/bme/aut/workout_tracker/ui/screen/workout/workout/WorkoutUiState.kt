package hu.bme.aut.workout_tracker.ui.screen.workout.workout

sealed class WorkoutUiState {

    data object WorkoutInit : WorkoutUiState()
    data class WorkoutLoaded(
        val weightList: List<List<String>>,
        val repsList: List<List<String>>,
        val isEnabledList: List<Boolean>,
    ) : WorkoutUiState()
}

sealed class WorkoutLoadedUiState {
    data object AddExercise : WorkoutLoadedUiState()
    data class Loaded(
        val pageCount: Int
    ) : WorkoutLoadedUiState()
}
