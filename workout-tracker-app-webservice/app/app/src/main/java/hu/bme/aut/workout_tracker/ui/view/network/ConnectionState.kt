package hu.bme.aut.workout_tracker.ui.view.network

sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}