package hu.bme.aut.workout_tracker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _isServerAvailable = MutableStateFlow(true)
    val isServerAvailable = _isServerAvailable.asStateFlow()

    fun updateServerAvailability() {
        workoutTrackerPresenter.isAvailable(
            onSuccess = {
                _isServerAvailable.value = true
            },
            onFailure = {
                _isServerAvailable.value = false
            }
        )
    }
}