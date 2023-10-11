package hu.bme.aut.workout_tracker.ui.screen.charts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsInit
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsLoaded
import hu.bme.aut.workout_tracker.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChartsUiState>(ChartsInit)
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    private var currentUser = User()

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun onSelectedExerciseChange(item: Exercise) {
        _uiState.update { (_uiState.value as ChartsLoaded).copy(selectedExercise = item) }
    }

    fun onSelectedChartChange(name: String) {
        _uiState.update { (_uiState.value as ChartsLoaded).copy(selectedChart = name) }
    }

    fun getExercises() {
        _uiState.value = ChartsLoaded(
            selectedExercise = Exercise(id = ""),
            selectedChart = Constants.chartsList[0]
        )
        viewModelScope.launch {
            currentUser = workoutTrackerPresenter.getCurrentUser()
            withContext(Dispatchers.IO) {
                workoutTrackerPresenter.getExercises().collect {
                    _exercises.postValue(it)
                }
            }
        }
    }

    fun getUserExercises(exercises: List<Exercise>): List<Exercise> {
        return exercises.filter { currentUser.volumeCharts.containsKey(it.id) }
            .filter { currentUser.volumeCharts[it.id]!!.size > 1 }
    }

    fun getSelectedChart(id: String, chartName: String): ChartEntryModel {
        when (chartName) {
            Constants.chartsList[0] -> {
                return getVolumeChartData(id)
            }

            Constants.chartsList[1] -> {
                return getAverageOneRepMaxChartData(id)
            }

            Constants.chartsList[2] -> {
                return getOneRepMaxChartData(id)
            }
        }
        return entryModelOf(0)
    }

    private fun getVolumeChartData(id: String): ChartEntryModel {
        var list = listOf(0)
        if (currentUser.volumeCharts.containsKey(id)) {
            if (currentUser.volumeCharts[id] != null)
                list = currentUser.volumeCharts[id]!!.toList()
        }
        val pointsData = mutableListOf<FloatEntry>()
        for (i in list.indices) {
            pointsData.add(FloatEntry(i.toFloat(), list[i].toFloat()))
        }
        return entryModelOf(pointsData.toList())
    }

    private fun getAverageOneRepMaxChartData(id: String): ChartEntryModel {
        var list = listOf(0.0)
        if (currentUser.averageOneRepMaxCharts.containsKey(id)) {
            if (currentUser.averageOneRepMaxCharts[id] != null)
                list = currentUser.averageOneRepMaxCharts[id]!!.toList()
        }
        val pointsData = mutableListOf<FloatEntry>()
        for (i in list.indices) {
            pointsData.add(FloatEntry(i.toFloat(), list[i].toFloat()))
        }
        return entryModelOf(pointsData.toList())
    }

    private fun getOneRepMaxChartData(id: String): ChartEntryModel {
        var list = listOf(0.0)
        if (currentUser.oneRepMaxCharts.containsKey(id)) {
            if (currentUser.oneRepMaxCharts[id] != null)
                list = currentUser.oneRepMaxCharts[id]!!.toList()
        }
        val pointsData = mutableListOf<FloatEntry>()
        for (i in list.indices) {
            pointsData.add(FloatEntry(i.toFloat(), list[i].toFloat()))
        }
        return entryModelOf(pointsData.toList())
    }
}