package hu.bme.aut.workout_tracker.ui.screen.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model_D.Exercise
import hu.bme.aut.workout_tracker.data.model_D.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingInit
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingLoaded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StandingViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<StandingUiState>(StandingInit)
    val uiState: StateFlow<StandingUiState> = _uiState.asStateFlow()

    private var currentUser = User()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun onSelectedItemChangeByName(name: String, exercises: List<Exercise>) {
        _uiState.update { (_uiState.value as StandingLoaded).copy(selectedItem = exercises.first { it.name == name }) }
    }

    fun getStandingExercises() {
        _uiState.value = StandingLoaded(selectedItem = Exercise(id = ""))
        getUsers()
        viewModelScope.launch {
            //currentUser = workoutTrackerPresenter.getCurrentUser()
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getStandingsExercises().collect {
                    _exercises.postValue(it)
                }
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getUsers().collect {
                    _users.postValue(it)
                }
            }
        }
    }

    fun getBestUsers(users: List<User>, exerciseId: String): List<User> {
        val list = mutableListOf<User>()
        for (u in users) {
            if (u.oneRepMaxCharts.containsKey(exerciseId)) {
                list.add(u)
            }
        }
        return list.toList().sortedBy { it.oneRepMaxCharts[exerciseId]!!.max() }.reversed()

    }

    fun isCurrentUser(u: User): Boolean {
        return u == currentUser
    }
}

