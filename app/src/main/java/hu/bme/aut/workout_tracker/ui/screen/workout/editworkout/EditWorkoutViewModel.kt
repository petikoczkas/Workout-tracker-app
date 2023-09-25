package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutInit
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutLoaded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditWorkoutViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditWorkoutUiState>(EditWorkoutInit)
    val uiState: StateFlow<EditWorkoutUiState> = _uiState.asStateFlow()

    var workout = Workout()

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun onNameChange(name: String) {
        _uiState.update { (_uiState.value as EditWorkoutLoaded).copy(name = name) }
    }

    fun getWorkout(workoutId: String) {
        viewModelScope.launch {
            workout = workoutTrackerPresenter.getWorkout(workoutId)
            _uiState.value = EditWorkoutLoaded(name = workout.name)
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getWorkoutExercises(workout).collect {
                    _exercises.postValue(it)
                }
            }
        }
    }
}