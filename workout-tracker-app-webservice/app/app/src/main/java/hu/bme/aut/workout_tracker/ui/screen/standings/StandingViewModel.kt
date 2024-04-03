package hu.bme.aut.workout_tracker.ui.screen.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingInit
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingUiState.StandingLoaded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StandingViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<StandingUiState>(StandingInit)
    val uiState: StateFlow<StandingUiState> = _uiState.asStateFlow()

    private var currentUser = User()

    private var charts = listOf<Chart>()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun onSelectedItemChangeByName(name: String, exercises: List<Exercise>) {
        _uiState.update { (_uiState.value as StandingLoaded).copy(selectedItem = exercises.first { it.name == name }) }
    }

    fun init() {
        _uiState.value = StandingLoaded(selectedItem = Exercise())
        viewModelScope.launch {
            currentUser = workoutTrackerPresenter.getCurrentUser()
            charts = workoutTrackerPresenter.getCharts()
            _exercises.value = workoutTrackerPresenter.getStandingsExercises()
            _users.value = workoutTrackerPresenter.getUsers()
        }
    }

    fun getBestUsers(users: List<User>, exerciseId: Int): Map<User, Double> {
        val map = mutableMapOf<User, Double>()
        for (c in charts) {
            if (c.type == ChartType.OneRepMax && c.exercise.id == exerciseId) {
                users.find { it.email == c.userId }?.let { map.put(it, c.data.max()) }
            }
        }
        return map.toList().sortedBy { (_, value) -> value }.reversed().take(50).toMap()

    }

    fun isCurrentUser(u: User): Boolean {
        return u == currentUser
    }
}

