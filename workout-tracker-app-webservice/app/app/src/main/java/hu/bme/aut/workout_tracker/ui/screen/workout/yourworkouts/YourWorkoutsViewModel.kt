package hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model_D.User
import hu.bme.aut.workout_tracker.data.model_D.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsUiState.YourWorkoutsInit
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsUiState.YourWorkoutsLoaded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class YourWorkoutsViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<YourWorkoutsUiState>(YourWorkoutsInit)
    val uiState: StateFlow<YourWorkoutsUiState> = _uiState.asStateFlow()

    private var currentUser = User()

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    fun getWorkouts() {
        _uiState.value = YourWorkoutsLoaded
        viewModelScope.launch {
            //currentUser = workoutTrackerPresenter.getCurrentUser()
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getUserWorkouts(currentUser).collect {
                    _workouts.postValue(it)
                }
            }
        }
    }

    fun isFavorite(id: String): Boolean {
        for (w in currentUser.favoriteWorkouts) {
            if (id == w) return true
        }
        return false
    }

    fun isFavoriteOnClick(id: String) {
        if (isFavorite(id)) currentUser.favoriteWorkouts.remove(id)
        else currentUser.favoriteWorkouts.add(id)
        viewModelScope.launch {
            workoutTrackerPresenter.updateUser(currentUser)
        }
    }
}