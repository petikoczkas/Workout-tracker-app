package hu.bme.aut.workout_tracker.ui.screen.charts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsInit
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsUiState.ChartsLoaded
import hu.bme.aut.workout_tracker.utils.AppData
import hu.bme.aut.workout_tracker.utils.AppData.currentUserEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val workoutTrackerPresenter: WorkoutTrackerPresenter
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChartsUiState>(ChartsInit)
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    private var currentUser = User()

    private var userCharts = listOf<Chart>()

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    fun onSelectedExerciseChange(item: Exercise) {
        _uiState.update { (_uiState.value as ChartsLoaded).copy(selectedExercise = item) }
    }

    fun onSelectedChartChange(name: String) {
        _uiState.update { (_uiState.value as ChartsLoaded).copy(selectedChart = name) }
    }

    fun onShowDialogChange(b: Boolean) {
        _uiState.update { (_uiState.value as ChartsLoaded).copy(showDialog = b) }
    }

    fun getExercises() {
        _uiState.value = ChartsLoaded(
            selectedExercise = Exercise(),
            selectedChart = AppData.chartsList[0],
            showDialog = false
        )
        viewModelScope.launch {
            currentUser = workoutTrackerPresenter.getCurrentUser()
            userCharts = workoutTrackerPresenter.getUserCharts(currentUserEmail)
            _exercises.value = workoutTrackerPresenter.getExercises()
        }
    }

    fun getUserExercises(exercises: List<Exercise>): List<Exercise> {
        return exercises.filter { e -> userCharts.any { it.exercise == e } }
            .filter { e -> userCharts.find { it.exercise == e }!!.data.size > 0 }
    }

    fun getSelectedChart(id: Int, chartName: String): ChartEntryModel {
        when (chartName) {
            AppData.chartsList[0] -> {
                return getVolumeChartData(id)
            }

            AppData.chartsList[1] -> {
                return getAverageOneRepMaxChartData(id)
            }

            AppData.chartsList[2] -> {
                return getOneRepMaxChartData(id)
            }
        }
        return entryModelOf(0)
    }

    private fun getVolumeChartData(id: Int): ChartEntryModel {
        var list = listOf(0.0)
        val volumeCharts = userCharts.filter { it.type == ChartType.Volume }
        if (volumeCharts.any { it.exercise.id == id }) {
            list = volumeCharts.find { it.exercise.id == id }!!.data
        }
        val pointsData = mutableListOf<FloatEntry>()
        for (i in list.indices) {
            pointsData.add(FloatEntry(i.toFloat(), list[i].toFloat()))
        }
        return entryModelOf(pointsData.toList())
    }

    private fun getAverageOneRepMaxChartData(id: Int): ChartEntryModel {
        var list = listOf(0.0)
        val averageOneRepMaxCharts = userCharts.filter { it.type == ChartType.AverageOneRepMax }
        if (averageOneRepMaxCharts.any { it.exercise.id == id }) {
            list = averageOneRepMaxCharts.find { it.exercise.id == id }!!.data
        }
        val pointsData = mutableListOf<FloatEntry>()
        for (i in list.indices) {
            pointsData.add(FloatEntry(i.toFloat(), list[i].toFloat()))
        }
        return entryModelOf(pointsData.toList())
    }

    private fun getOneRepMaxChartData(id: Int): ChartEntryModel {
        var list = listOf(0.0)
        val oneRepMaxCharts = userCharts.filter { it.type == ChartType.OneRepMax }
        if (oneRepMaxCharts.any { it.exercise.id == id }) {
            list = oneRepMaxCharts.find { it.exercise.id == id }!!.data
        }
        val pointsData = mutableListOf<FloatEntry>()
        for (i in list.indices) {
            pointsData.add(FloatEntry(i.toFloat(), list[i].toFloat()))
        }
        return entryModelOf(pointsData.toList())
    }
}