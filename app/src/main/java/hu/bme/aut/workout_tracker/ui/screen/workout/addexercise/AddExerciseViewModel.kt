package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseInit
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseLoaded
import hu.bme.aut.workout_tracker.utils.Constants.BODY_PARTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddExerciseUiState>(AddExerciseInit)
    val uiState: StateFlow<AddExerciseUiState> = _uiState.asStateFlow()

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun onSelectedItemChange(item: String) {
        _uiState.update { (_uiState.value as AddExerciseLoaded).copy(selectedItem = item) }
    }

    fun onShowDialogChange(b: Boolean) {
        _uiState.update { (_uiState.value as AddExerciseLoaded).copy(showDialog = b) }
    }

    fun onNewExerciseChange(exercise: String) {
        _uiState.update { (_uiState.value as AddExerciseLoaded).copy(newExercise = exercise) }
    }

    fun getExercises() {
        _uiState.value = AddExerciseLoaded(
            selectedItem = BODY_PARTS[0],
            showDialog = false,
            newExercise = ""
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getExercises().collect {
                    _exercises.postValue(it)
                }
            }
        }
    }

    fun getExercisesByCategory(exercises: List<Exercise>?): List<Exercise> {
        return exercises!!.filter { it.category == (_uiState.value as AddExerciseLoaded).selectedItem }
    }
}