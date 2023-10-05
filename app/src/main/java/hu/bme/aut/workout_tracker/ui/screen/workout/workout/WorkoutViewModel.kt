package hu.bme.aut.workout_tracker.ui.screen.workout.workout

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
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutLoadedUiState.Loaded
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutUiState.WorkoutInit
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutUiState.WorkoutLoaded
import hu.bme.aut.workout_tracker.utils.Constants.addedExercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutInit)
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    private val _loadedUiState = MutableStateFlow<WorkoutLoadedUiState>(Loaded(pageCount = 0))
    val loadedUiState: StateFlow<WorkoutLoadedUiState> = _loadedUiState.asStateFlow()

    private var _workout = Workout()
    val workout = _workout

    private var currentUser = User()

    private val _workoutExercises = MutableLiveData<List<Exercise>>()
    val workoutExercises: LiveData<List<Exercise>> = _workoutExercises

    private val _exercises = mutableStateListOf<Exercise>()
    val exercises: List<Exercise> = _exercises

    private val _saveFailedEvent =
        MutableStateFlow(SaveFailed(isSaveFailed = false, exception = null))
    val saveFailedEvent = _saveFailedEvent.asStateFlow()

    fun onWeightListChange(weight: String, pageNum: Int, setNum: Int) {
        val list = (_uiState.value as WorkoutLoaded).weightList.toMutableList()
        val page = list[pageNum].toMutableList()
        page[setNum] = weight
        list[pageNum] = page
        _uiState.update { (_uiState.value as WorkoutLoaded).copy(weightList = list.toList()) }
    }

    fun onRepsListChange(reps: String, pageNum: Int, setNum: Int) {
        val list = (_uiState.value as WorkoutLoaded).repsList.toMutableList()
        val page = list[pageNum].toMutableList()
        page[setNum] = reps
        list[pageNum] = page
        _uiState.update { (_uiState.value as WorkoutLoaded).copy(repsList = list.toList()) }
    }

    fun getWorkout(workoutId: String) {
        viewModelScope.launch {
            _workout = workoutTrackerPresenter.getWorkout(workoutId)
            _loadedUiState.update { (_loadedUiState.value as Loaded).copy(pageCount = _workout.exercises.size + 1) }
            currentUser = workoutTrackerPresenter.getCurrentUser()
            setDefaultValues()
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getWorkoutExercises(_workout).collect {
                    _workoutExercises.postValue(it)
                    setAddedExercises(it)
                }
            }
        }
    }

    private fun setAddedExercises(exercises: List<Exercise>) {
        clearAddedExercises()
        for (w in _workout.exercises) {
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

    private fun setDefaultValues() {
        val weight = mutableListOf<String>()
        val reps = mutableListOf<String>()
        val weightList = mutableListOf<List<String>>()
        val repsList = mutableListOf<List<String>>()
        val enabledList = mutableListOf<Boolean>()
        for (eId in _workout.exercises) {
            var setList = listOf<String>()
            for (userExercise in currentUser.exercises) {
                if (userExercise.key == eId) {
                    setList = userExercise.value
                }
            }
            if (setList.isEmpty()) {
                weight.add("")
                reps.add("")
            } else {
                for (s in setList) {
                    val tmp = s.split(" ")
                    weight.add(tmp[0])
                    reps.add(tmp[1])
                }
            }
            weightList.add(weight.toList())
            repsList.add(reps.toList())
            weight.clear()
            reps.clear()
            enabledList.add(true)
        }
        _uiState.value = WorkoutLoaded(
            weightList = weightList.toList(),
            repsList = repsList.toList(),
            isEnabledList = enabledList.toList()
        )
    }

    fun getExerciseName(page: Int): String {
        for (e in _exercises) {
            if (e.id == _workout.exercises[page]) return e.name
        }
        return ""
    }

    fun addButtonOnClick(page: Int) {
        val weightList = (_uiState.value as WorkoutLoaded).weightList.toMutableList()
        val weightPageList = (_uiState.value as WorkoutLoaded).weightList[page].toMutableList()
        val repsList = (_uiState.value as WorkoutLoaded).repsList.toMutableList()
        val repsPageList = (_uiState.value as WorkoutLoaded).repsList[page].toMutableList()

        weightPageList.add(weightPageList.last())
        weightList[page] = weightPageList
        repsPageList.add(repsPageList.last())
        repsList[page] = repsPageList

        _uiState.update {
            (_uiState.value as WorkoutLoaded).copy(
                weightList = weightList,
                repsList = repsList
            )
        }
    }

    fun removeButtonOnClick(page: Int) {
        val weightList = (_uiState.value as WorkoutLoaded).weightList.toMutableList()
        val weightPageList = (_uiState.value as WorkoutLoaded).weightList[page].toMutableList()
        val repsList = (_uiState.value as WorkoutLoaded).repsList.toMutableList()
        val repsPageList = (_uiState.value as WorkoutLoaded).repsList[page].toMutableList()

        weightPageList.removeLast()
        weightList[page] = weightPageList
        repsPageList.removeLast()
        repsList[page] = repsPageList

        _uiState.update {
            (_uiState.value as WorkoutLoaded).copy(
                weightList = weightList,
                repsList = repsList
            )
        }
    }

    fun switchOrAddCurrentExercise(pageString: String): Boolean {
        val page = if (pageString == "addExercise") addedExercises.size - 1 else pageString.toInt()
        if (addedExercises.size > _workout.exercises.size) {
            if (pageString == "addExercise") changeLoadedUiState(Loaded(pageCount = addedExercises.size + 1))
            var newExercise = Exercise()
            for (e in addedExercises) {
                if (!_workout.exercises.contains(e.id)) {
                    newExercise = e
                    break
                }
            }
            if (page == _workout.exercises.size) _workout.exercises.add(page, newExercise.id)
            else _workout.exercises[page] = newExercise.id
            val list = _exercises
            for (i in list.indices) {
                if (list[i].id == _workout.exercises[page]) {
                    list[i] = newExercise
                }
            }
            _workoutExercises.value = list
            if (pageString != "addExercise") setAddedExercises(list)

            val weightList = (_uiState.value as WorkoutLoaded).weightList.toMutableList()
            val weightPageList = mutableListOf<String>()
            val repsList = (_uiState.value as WorkoutLoaded).repsList.toMutableList()
            val repsPageList = mutableListOf<String>()
            val isEnabledList = (_uiState.value as WorkoutLoaded).isEnabledList.toMutableList()

            val setList: List<String>
            if (currentUser.exercises.contains(newExercise.id)) {
                setList = currentUser.exercises[newExercise.id]!!
                for (s in setList) {
                    val tmp = s.split(" ")
                    weightPageList.add(tmp[0])
                    repsPageList.add(tmp[1])
                }
            } else {
                weightPageList.add("")
                repsPageList.add("")
            }
            if (page == weightList.size) {
                weightList.add(page, weightPageList)
                repsList.add(page, repsPageList)
                isEnabledList.add(page, true)
            } else {
                weightList[page] = weightPageList
                repsList[page] = repsPageList

            }

            _uiState.update {
                (_uiState.value as WorkoutLoaded).copy(
                    weightList = weightList,
                    repsList = repsList,
                    isEnabledList = isEnabledList
                )
            }
            return !currentUser.exercises.contains(newExercise.id)
        }
        return false
    }

    fun isSaveButtonEnabled(page: Int): Boolean {
        val weightPageList = (_uiState.value as WorkoutLoaded).weightList[page]
        val repsPageList = (_uiState.value as WorkoutLoaded).repsList[page]
        return !(weightPageList.contains("") || repsPageList.contains(""))

    }

    fun saveButtonOnClick(page: Int) {
        val weightPageList = (_uiState.value as WorkoutLoaded).weightList[page]
        val repsPageList = (_uiState.value as WorkoutLoaded).repsList[page]

        disableCurrentPage(page)
        val pageList = mutableListOf<String>()
        var volume = 0
        val exerciseId = _workout.exercises[page]
        for (i in weightPageList.indices) {
            pageList.add("${weightPageList[i]} ${repsPageList[i]}")
            volume += weightPageList[i].toInt() * repsPageList[i].toInt()
        }
        var volumeList = currentUser.charts[exerciseId]
        if (volumeList == null) {
            volumeList = mutableListOf(volume)
        } else {
            volumeList.add(volume)
        }
        currentUser.exercises[exerciseId] = pageList
        currentUser.charts[exerciseId] = volumeList
    }

    fun endWorkoutOnClick() {
        viewModelScope.launch {
            try {
                workoutTrackerPresenter.updateUser(user = currentUser)
            } catch (e: Exception) {
                _saveFailedEvent.value = SaveFailed(
                    isSaveFailed = true,
                    exception = e
                )
            }
        }
    }

    private fun disableCurrentPage(pageNum: Int) {
        val list = (_uiState.value as WorkoutLoaded).isEnabledList.toMutableList()
        list[pageNum] = false
        _uiState.update { (_uiState.value as WorkoutLoaded).copy(isEnabledList = list.toList()) }
    }

    fun handledSaveFailedEvent() {
        _saveFailedEvent.update { _saveFailedEvent.value.copy(isSaveFailed = false) }
    }

    fun changeLoadedUiState(value: WorkoutLoadedUiState) {
        _loadedUiState.value = value
    }

    data class SaveFailed(
        val isSaveFailed: Boolean,
        val exception: Exception?
    )
}