package hu.bme.aut.workout_tracker.ui.screen.workout.workout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.data.model.*
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class WorkoutViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WorkoutViewModel
    private val mockPresenter = mockk<WorkoutTrackerPresenter>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private val currentUserEmail = "test@example.com"
    private val exercises = listOf(
        Exercise(id = 1, category = "Legs", name = "Squat"),
        Exercise(id = 2, category = "Arms", name = "Curl")
    )
    private val workout = Workout(
        id = 1,
        userId = currentUserEmail,
        name = "Test Workout",
        isFavorite = false,
        exercises = exercises.toMutableList()
    )
    private val savedExercises = listOf(
        SavedExercise(
            id = 1,
            userId = currentUserEmail,
            exercise = exercises[0],
            data = mutableListOf("100 10")
        ),
        SavedExercise(
            id = 2,
            userId = currentUserEmail,
            exercise = exercises[1],
            data = mutableListOf("50 15")
        )
    )
    private val charts = listOf(
        Chart(
            id = 1,
            userId = currentUserEmail,
            exercise = exercises[0],
            type = ChartType.Volume,
            data = mutableListOf(1000.0)
        ),
        Chart(
            id = 2,
            userId = currentUserEmail,
            exercise = exercises[1],
            type = ChartType.AverageOneRepMax,
            data = mutableListOf(200.0)
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WorkoutViewModel(mockPresenter)
        coEvery { mockPresenter.getWorkout(any()) } returns workout
        coEvery { mockPresenter.getCurrentUser() } returns User(
            email = currentUserEmail,
            firstName = "",
            lastName = "",
            photo = byteArrayOf()
        )
        coEvery { mockPresenter.getUserSavedExercises(any()) } returns savedExercises
        coEvery { mockPresenter.getUserCharts(any()) } returns charts
        coEvery { mockPresenter.updateChart(any()) } returns Unit
        coEvery { mockPresenter.updateSavedExercise(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWorkout should update UI state and set default values`() = runTest {
        viewModel.getWorkout(1)
        advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assert(uiState is WorkoutUiState.WorkoutLoaded)
        val loadedUiState = viewModel.loadedUiState.value
        assert(loadedUiState is WorkoutLoadedUiState.Loaded)
        assertThat((uiState as WorkoutUiState.WorkoutLoaded).weightList).isNotEmpty()
        assertThat((uiState as WorkoutUiState.WorkoutLoaded).repsList).isNotEmpty()
    }

    @Test
    fun `onWeightListChange should update weight list in UI state`() = runTest {
        viewModel.getWorkout(1)
        advanceUntilIdle()
        viewModel.onWeightListChange("250", 0, 0)

        val uiState = viewModel.uiState.value
        assertThat((uiState as WorkoutUiState.WorkoutLoaded).weightList[0][0]).isEqualTo("250")
    }

    @Test
    fun `addButtonOnClick should add new entry to weight and reps lists`() = runTest {
        viewModel.getWorkout(1)
        advanceUntilIdle()
        viewModel.addButtonOnClick(0)

        val uiState = viewModel.uiState.value as WorkoutUiState.WorkoutLoaded
        assertThat(uiState.weightList[0]).hasSize(2)
        assertThat(uiState.repsList[0]).hasSize(2)
    }

    @Test
    fun `handledSaveFailedEvent should reset save failed state`() {
        viewModel.handledSaveFailedEvent()

        assertThat(viewModel.saveFailedEvent.value.isSaveFailed).isFalse()
    }
}
