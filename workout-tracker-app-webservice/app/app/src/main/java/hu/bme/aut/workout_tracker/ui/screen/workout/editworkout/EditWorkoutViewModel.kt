package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutInit
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutLoaded
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutUiState.EditWorkoutSaved
import hu.bme.aut.workout_tracker.utils.Constants.addedExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditWorkoutViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditWorkoutUiState>(EditWorkoutInit)
    val uiState: StateFlow<EditWorkoutUiState> = _uiState.asStateFlow()

    private var workout = Workout()

    private var currentUser = User()

    private val _workoutExercises = MutableLiveData<List<Exercise>>()
    val workoutExercises: LiveData<List<Exercise>> = _workoutExercises

    private val _exercises = mutableStateListOf<Exercise>()
    val exercises: List<Exercise> = _exercises

    private val removedExercises = mutableListOf<Exercise>()

    private val _updateWorkoutFailedEvent =
        MutableStateFlow(
            UpdateWorkoutFailure(
                isUpdateWorkoutFailed = false,
                exception = null
            )
        )
    val updateWorkoutFailedEvent = _updateWorkoutFailedEvent.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { (_uiState.value as EditWorkoutLoaded).copy(name = name) }
    }

    fun getWorkout(workoutId: String) {
        clearAddedExercises()
        viewModelScope.launch {
            currentUser = workoutTrackerPresenter.getCurrentUser()
            workout = workoutTrackerPresenter.getWorkout(workoutId)
            _uiState.value = EditWorkoutLoaded(name = workout.name)
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getWorkoutExercises(workout).collect {
                    _workoutExercises.postValue(it)
                    setAddedExercises(it)
                }
            }
        }
    }

    private fun setAddedExercises(exercises: List<Exercise>) {
        for (w in workout.exercises) {
            for (e in exercises) {
                if (w == e.id)
                    if (!addedExercises.contains(e))
                        addedExercises.add(e)
            }
        }
    }

    fun getWorkoutExercises() {
        _exercises.clear()
        for (e in addedExercises) {
            _exercises.add(e)
        }
    }

    private fun clearAddedExercises() {
        addedExercises.clear()
    }

    fun removeButtonOnClick(e: Exercise) {
        addedExercises.remove(e)
        _exercises.remove(e)
        removedExercises.add(e)
    }

    fun saveButtonOnClick(workoutId: String) {
        val id: String
        if (workoutId == "create") {
            id = UUID.randomUUID().toString()
            currentUser.workouts.add(id)
        } else {
            id = workoutId
        }
        val name = (_uiState.value as EditWorkoutLoaded).name
        val tmp = mutableListOf<String>()
        for (e in exercises) tmp.add(e.id)
        viewModelScope.launch {
            try {
                workoutTrackerPresenter.updateWorkout(
                    Workout(
                        id = id,
                        name = name,
                        user = currentUser.id,
                        exercises = tmp,
                    )
                )
                workoutTrackerPresenter.updateUser(currentUser)
                _uiState.value = EditWorkoutSaved
            } catch (e: Exception) {
                _updateWorkoutFailedEvent.value = UpdateWorkoutFailure(
                    isUpdateWorkoutFailed = true,
                    exception = e
                )
            }
        }
    }

    fun handledUpdateWorkoutFailedEvent() {
        _updateWorkoutFailedEvent.update {
            _updateWorkoutFailedEvent.value.copy(
                isUpdateWorkoutFailed = false
            )
        }
    }

    data class UpdateWorkoutFailure(
        val isUpdateWorkoutFailed: Boolean, val exception: Exception?
    )

}