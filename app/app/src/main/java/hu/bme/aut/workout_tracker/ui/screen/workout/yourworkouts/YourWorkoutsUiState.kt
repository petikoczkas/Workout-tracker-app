package hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts

sealed class YourWorkoutsUiState {
    data object YourWorkoutsInit : YourWorkoutsUiState()

    data object YourWorkoutsLoaded : YourWorkoutsUiState()
}

