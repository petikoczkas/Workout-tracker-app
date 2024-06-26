package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseInit
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseUiState.AddExerciseLoaded
import hu.bme.aut.workout_tracker.utils.Constants.BODY_PARTS
import hu.bme.aut.workout_tracker.utils.Constants.addedExercises
import hu.bme.aut.workout_tracker.utils.capitalizeWords
import hu.bme.aut.workout_tracker.utils.removeEmptyLines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddExerciseUiState>(AddExerciseInit)
    val uiState: StateFlow<AddExerciseUiState> = _uiState.asStateFlow()

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    var workout = Workout()

    private val _createExerciseFailedEvent =
        MutableStateFlow(
            CreateExerciseFailure(
                isCreateExerciseFailed = false,
                exception = null
            )
        )
    val createExerciseFailedEvent = _createExerciseFailedEvent.asStateFlow()

    fun onSelectedItemChange(item: String) {
        _uiState.update { (_uiState.value as AddExerciseLoaded).copy(selectedItem = item) }
    }

    fun onShowDialogChange(b: Boolean) {
        _uiState.update { (_uiState.value as AddExerciseLoaded).copy(showDialog = b) }
    }

    fun onNewExerciseChange(exercise: String) {
        _uiState.update { (_uiState.value as AddExerciseLoaded).copy(newExercise = exercise) }
    }

    fun getExercises(workoutId: Int) {
        _uiState.value = AddExerciseLoaded(
            selectedItem = BODY_PARTS[0],
            showDialog = false,
            newExercise = ""
        )
        viewModelScope.launch {
            if (workoutId != -1) {
                workout = workoutTrackerPresenter.getWorkout(workoutId)
            }
            _exercises.value = workoutTrackerPresenter.getExercises()
        }
    }

    fun getExercisesByCategory(exercises: List<Exercise>?): List<Exercise> {
        return exercises!!.filter {
            it.category ==
                    (_uiState.value as AddExerciseLoaded).selectedItem &&
                    !addedExercises.contains(it)
        }
    }

    fun dialogSaveButtonOnClick(exercises: List<Exercise>, context: Context) {
        val category = (_uiState.value as AddExerciseLoaded).selectedItem
        val name =
            (_uiState.value as AddExerciseLoaded).newExercise.removeEmptyLines().capitalizeWords()
        onNewExerciseChange("")
        if (exercises.none {
                it.name.lowercase().replace(" ", "") == name.lowercase().replace(" ", "")
            }) {
            viewModelScope.launch {
                try {
                    workoutTrackerPresenter.createExercise(
                        Exercise(
                            id = -1,
                            category = category,
                            name = name
                        )
                    )
                    _exercises.value = workoutTrackerPresenter.getExercises()
                } catch (e: Exception) {
                    _createExerciseFailedEvent.value = CreateExerciseFailure(
                        isCreateExerciseFailed = true,
                        exception = e
                    )
                }
            }
        } else {
            _createExerciseFailedEvent.value = CreateExerciseFailure(
                isCreateExerciseFailed = true,
                exception = Exception(context.getString(R.string.exercise_already_exists))
            )
        }
    }

    fun saveExercise(e: Exercise) {
        addedExercises.add(e)
    }

    fun handledCreateExerciseFailedEvent() {
        _createExerciseFailedEvent.update {
            _createExerciseFailedEvent.value.copy(
                isCreateExerciseFailed = false
            )
        }
    }

    data class CreateExerciseFailure(
        val isCreateExerciseFailed: Boolean, val exception: Exception?
    )
}