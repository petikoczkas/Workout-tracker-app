package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.utils.Constants
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()


    private lateinit var viewModel: HomeViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        workoutTrackerPresenter = mockk(relaxed = true)
        viewModel = HomeViewModel(workoutTrackerPresenter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test init sets uiState to HomeLoaded and fetches data`() = runTest {
        val user = User(email = "example@test.com", firstName = "John", lastName = "Doe", byteArrayOf())
        val charts = listOf(
            Chart(
                id = 0,
                userId = "0",
                type = ChartType.OneRepMax,
                exercise = Exercise(id = 0, category = "Chest", name = "Bench Press"),
                data = mutableListOf(100.0)
            )
        )
        val exercises = listOf(Exercise())

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns user
        coEvery { workoutTrackerPresenter.getUserCharts(Constants.currentUserEmail) } returns charts
        coEvery { workoutTrackerPresenter.getStandingsExercises() } returns exercises

        viewModel.init()

        assertThat(viewModel.uiState.first()).isEqualTo(HomeUiState.HomeLoaded)
        assertThat(viewModel.exercises.value).isEqualTo(exercises)
        assertThat(viewModel.getUserFirstName()).isEqualTo("John")
    }

    @Test
    fun `test getFavoriteWorkouts fetches favorite workouts`() = runTest {
        val favoriteWorkouts = listOf(Workout())

        coEvery { workoutTrackerPresenter.getUserFavoriteWorkouts(Constants.currentUserEmail) } returns favoriteWorkouts

        viewModel.getFavoriteWorkouts()

        assertThat(viewModel.workouts.value).isEqualTo(favoriteWorkouts)
    }

    @Test
    fun `test getMaxList returns correct max list`() = runTest {
        val exercises = listOf(
            Exercise(id = 0, category = "Chest", name = "Barbell Bench Press"),
            Exercise(id = 1, category = "Back", name = "Barbell Deadlift"),
            Exercise(id = 3, category = "Legs", name = "Barbell Squat")
        )
        val charts = listOf(
            Chart(
                id = 0,
                userId = "0",
                type = ChartType.OneRepMax,
                exercise = exercises[0],
                data = mutableListOf(100.0)
            ),
            Chart(
                id = 1,
                userId = "0",
                type = ChartType.OneRepMax,
                exercise = exercises[1],
                data = mutableListOf(200.0)
            )
        )

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns User()
        coEvery { workoutTrackerPresenter.getUserCharts(Constants.currentUserEmail) } returns charts

        viewModel.init()

        val maxList = viewModel.getMaxList(exercises)

        assertThat(maxList).isEqualTo(
            listOf(
                listOf("Bench Press", "100"),
                listOf("Deadlift", "200"),
                listOf("Squat", "-")
            )
        )
    }
}
