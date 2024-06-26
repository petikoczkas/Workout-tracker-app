package hu.bme.aut.workout_tracker.ui.screen.workout.workout

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.SavedExercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutLoadedUiState.Loaded
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutUiState.WorkoutInit
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutUiState.WorkoutLoaded
import hu.bme.aut.workout_tracker.utils.Constants.addedExercises
import hu.bme.aut.workout_tracker.utils.Constants.currentUserEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
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

    private var userSavedExercises = listOf<SavedExercise>()

    private var userCharts = listOf<Chart>()

    private val _exercises = mutableStateListOf<Exercise>()
    val exercises: List<Exercise> = _exercises

    private var savedUserSavedExercises = mutableListOf<SavedExercise>()
    private var savedCharts = mutableListOf<Chart>()

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

    fun getWorkout(workoutId: Int) {
        viewModelScope.launch {
            _workout = workoutTrackerPresenter.getWorkout(workoutId)
            _loadedUiState.update { (_loadedUiState.value as Loaded).copy(pageCount = _workout.exercises.size + 1) }
            currentUser = workoutTrackerPresenter.getCurrentUser()
            userSavedExercises = workoutTrackerPresenter.getUserSavedExercises(currentUserEmail)
            userCharts = workoutTrackerPresenter.getUserCharts(currentUserEmail)
            setDefaultValues()
            setAddedExercises(_workout.exercises)
        }
    }

    private fun setAddedExercises(exercises: List<Exercise>) {
        clearAddedExercises()
        for (w in _workout.exercises) {
            for (e in exercises) {
                if (w.id == e.id) {
                    if (!addedExercises.contains(e)) {
                        addedExercises.add(e)
                    }
                }
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
        for (exercise in _workout.exercises) {
            var setList = listOf<String>()
            for (savedExercise in userSavedExercises) {
                if (savedExercise.exercise == exercise) {
                    setList = savedExercise.data
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
            if (e == _workout.exercises[page]) return e.name
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
                if (!_workout.exercises.any { it == e }) {
                    newExercise = e
                    break
                }
            }
            if (page == _workout.exercises.size) _workout.exercises.add(page, newExercise)
            else _workout.exercises[page] = newExercise
            val list = _exercises
            for (i in list.indices) {
                if (list[i] == _workout.exercises[page]) {
                    list[i] = newExercise
                }
            }
            if (pageString != "addExercise") setAddedExercises(list)

            val weightList = (_uiState.value as WorkoutLoaded).weightList.toMutableList()
            val weightPageList = mutableListOf<String>()
            val repsList = (_uiState.value as WorkoutLoaded).repsList.toMutableList()
            val repsPageList = mutableListOf<String>()
            val isEnabledList = (_uiState.value as WorkoutLoaded).isEnabledList.toMutableList()

            val setList: List<String>
            if (userSavedExercises.any { it.exercise == newExercise }) {
                setList = userSavedExercises.find { it.exercise == newExercise }!!.data
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
            return !userSavedExercises.any { it.exercise == newExercise }
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
        var volume = 0.0
        var maxOneRepMax = 0.0
        var averageOneRepMax = 0.0
        val exercise = _workout.exercises[page]
        for (i in weightPageList.indices) {
            pageList.add("${weightPageList[i]} ${repsPageList[i]}")
            volume += weightPageList[i].toInt() * repsPageList[i].toInt()
            val estimatedOneRepMax =
                weightPageList[i].toDouble() * (1 + repsPageList[i].toDouble() / 30)
            averageOneRepMax += estimatedOneRepMax
            if (estimatedOneRepMax > maxOneRepMax) maxOneRepMax = estimatedOneRepMax
        }
        averageOneRepMax /= weightPageList.size
        averageOneRepMax =
            averageOneRepMax.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        maxOneRepMax = maxOneRepMax.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        var volumeList = userCharts.find { it.type == ChartType.Volume && it.exercise == exercise }
        if (volumeList == null) {
            volumeList = Chart(
                id = -1,
                userId = currentUserEmail,
                exercise = exercise,
                type = ChartType.Volume,
                data = mutableListOf(volume)
            )
        } else {
            volumeList.data.add(volume)
        }
        var averageOneRepMaxList =
            userCharts.find { it.type == ChartType.AverageOneRepMax && it.exercise == exercise }
        if (averageOneRepMaxList == null) {
            averageOneRepMaxList = Chart(
                id = -1,
                userId = currentUserEmail,
                exercise = exercise,
                type = ChartType.AverageOneRepMax,
                data = mutableListOf(averageOneRepMax)
            )
        } else {
            averageOneRepMaxList.data.add(averageOneRepMax)
        }
        var oneRepMaxList =
            userCharts.find { it.type == ChartType.OneRepMax && it.exercise == exercise }
        if (oneRepMaxList == null) {
            oneRepMaxList = Chart(
                id = -1,
                userId = currentUserEmail,
                exercise = exercise,
                type = ChartType.OneRepMax,
                data = mutableListOf(maxOneRepMax)
            )
        } else {
            oneRepMaxList.data.add(maxOneRepMax)
        }

        if (userSavedExercises.any { it.exercise == exercise }) {
            val e = userSavedExercises.find { it.exercise == exercise }
            e!!.data = pageList
            savedUserSavedExercises.add(e)
        } else {
            savedUserSavedExercises.add(
                SavedExercise(
                    id = -1,
                    userId = currentUserEmail,
                    exercise = exercise,
                    data = pageList
                )
            )
        }

        savedCharts.add(volumeList)
        savedCharts.add(averageOneRepMaxList)
        savedCharts.add(oneRepMaxList)
    }

    fun endWorkoutOnClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    for (chart in savedCharts) {
                        workoutTrackerPresenter.updateChart(chart)
                    }
                    for (exercise in savedUserSavedExercises) {
                        workoutTrackerPresenter.updateSavedExercise(exercise)
                    }
                } catch (e: Exception) {
                    _saveFailedEvent.value = SaveFailed(
                        isSaveFailed = true,
                        exception = e
                    )
                }
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