package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeInit
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeLoaded
import hu.bme.aut.workout_tracker.utils.Constants.currentUserEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeInit)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentUser = User()

    private var userCharts = listOf<Chart>()

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun getUserFirstName(): String {
        return currentUser.firstName
    }

    fun init() {
        _uiState.value = HomeLoaded
        getFavoriteWorkouts()
        viewModelScope.launch {
            currentUser = workoutTrackerPresenter.getCurrentUser()
            userCharts = workoutTrackerPresenter.getUserCharts(currentUserEmail)
            _exercises.value = workoutTrackerPresenter.getStandingsExercises()
        }
    }

    fun getFavoriteWorkouts() {
        viewModelScope.launch {
            _workouts.value = workoutTrackerPresenter.getUserFavoriteWorkouts(currentUserEmail)
        }
    }

    fun getMaxList(exercises: List<Exercise>): List<List<String>> {
        val maxList = mutableListOf<List<String>>()
        for (e in exercises) {
            val tmp = mutableListOf<String>()
            tmp.add(e.name.substringAfter(" "))
            val chart = userCharts.find { it.type == ChartType.OneRepMax && it.exercise == e }
            if (chart != null) {
                tmp.add(chart.data.max().toInt().toString())
            } else {
                tmp.add("-")
            }
            maxList.add(tmp.toList())
        }
        return maxList.toList()
    }
}