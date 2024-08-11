package hu.bme.aut.workout_tracker.ui.screen.charts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.utils.Constants
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
class ChartsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: ChartsViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        workoutTrackerPresenter = mockk()
        viewModel = ChartsViewModel(workoutTrackerPresenter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getExercises updates LiveData`() = runTest {
        val dummyUser = User()
        val dummyCharts = listOf(
            Chart(
                id = 0,
                userId = "0",
                exercise = Exercise(),
                type = ChartType.Volume,
                data = mutableListOf()
            ),
            Chart(
                id = 1,
                userId = "0",
                exercise = Exercise(),
                type = ChartType.AverageOneRepMax,
                data = mutableListOf()
            )
        )
        val dummyExercises = listOf(Exercise())

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns dummyUser
        coEvery { workoutTrackerPresenter.getUserCharts(any()) } returns dummyCharts
        coEvery { workoutTrackerPresenter.getExercises() } returns dummyExercises

        val observer: Observer<List<Exercise>> = mockk(relaxed = true)
        viewModel.exercises.observeForever(observer)

        viewModel.getExercises()

        coVerify { workoutTrackerPresenter.getCurrentUser() }
        coVerify { workoutTrackerPresenter.getUserCharts(any()) }
        coVerify { workoutTrackerPresenter.getExercises() }

        assertThat(viewModel.exercises.value).isEqualTo(dummyExercises)

        viewModel.exercises.removeObserver(observer)
    }

    @Test
    fun `test onSelectedExerciseChange updates state`() {
        viewModel.getExercises()
        val dummyExercise = Exercise()
        viewModel.onSelectedExerciseChange(dummyExercise)

        val uiState = viewModel.uiState.value
        if (uiState is ChartsUiState.ChartsLoaded) {
            assertThat(uiState.selectedExercise).isEqualTo(dummyExercise)
        } else {
            throw AssertionError("UI state is not ChartsLoaded")
        }
    }

    @Test
    fun `test onSelectedChartChange updates state`() {
        viewModel.getExercises()
        val dummyChart = Constants.chartsList[0]
        viewModel.onSelectedChartChange(dummyChart)

        val uiState = viewModel.uiState.value
        if (uiState is ChartsUiState.ChartsLoaded) {
            assertThat(uiState.selectedChart).isEqualTo(dummyChart)
        } else {
            throw AssertionError("UI state is not ChartsLoaded")
        }
    }

    @Test
    fun `test getSelectedChart returns correct data`() {
        val dummyChart = Chart(
            id = 0,
            userId = "0",
            exercise = Exercise(),
            type = ChartType.Volume,
            data = mutableListOf()
        )
        coEvery { workoutTrackerPresenter.getUserCharts(any()) } returns listOf(dummyChart)

        val chartData = viewModel.getSelectedChart(1, Constants.chartsList[0])
        assertThat(chartData.entries[0].size).isEqualTo(1)
    }
}
