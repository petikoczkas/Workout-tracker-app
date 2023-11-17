package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeInit
import hu.bme.aut.workout_tracker.ui.screen.home.HomeUiState.HomeLoaded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeInit)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentUser = User()

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun getUserLastName(): String {
        return currentUser.name.substringAfter(" ")
    }

    fun getFavoriteWorkouts() {
        _uiState.value = HomeLoaded
        getStandingsExercises()
        viewModelScope.launch {
            currentUser = workoutTrackerPresenter.getCurrentUser()
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getUserFavoriteWorkouts(currentUser).collect {
                    _workouts.postValue(it)
                }
            }
        }
    }

    private fun getStandingsExercises() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getStandingsExercises().collect {
                    _exercises.postValue(it)
                }
            }
        }
    }

    fun getMaxList(exercises: List<Exercise>): List<List<String>> {
        val maxList = mutableListOf<List<String>>()
        for (e in exercises) {
            val tmp = mutableListOf<String>()
            tmp.add(e.name.substringAfter(" "))
            if (currentUser.oneRepMaxCharts.containsKey(e.id)) {
                tmp.add(currentUser.oneRepMaxCharts[e.id]?.max()?.toInt().toString())
            } else {
                tmp.add("-")
            }
            maxList.add(tmp.toList())
        }
        return maxList.toList()
    }
}