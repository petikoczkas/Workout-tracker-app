package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
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
class EditWorkoutViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditWorkoutViewModel
    private val mockPresenter = mockk<WorkoutTrackerPresenter>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val currentUser = User(email = "test@example.com", firstName = "John", lastName = "Doe", photo = byteArrayOf())
    private val exercises = listOf(
        Exercise(id = 1, category = "Legs", name = "Squat"),
        Exercise(id = 2, category = "Arms", name = "Curl")
    )
    private val workout = Workout(
        id = 1,
        userId = "test@example.com",
        name = "Test Workout",
        isFavorite = false,
        exercises = exercises.toMutableList()
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditWorkoutViewModel(mockPresenter)
        coEvery { mockPresenter.getCurrentUser() } returns currentUser
        coEvery { mockPresenter.getWorkout(any()) } returns workout
        coEvery { mockPresenter.updateWorkout(any()) } returns Unit
        coEvery { mockPresenter.deleteWorkout(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWorkout should update uiState and exercises`() = runTest {
        viewModel.getWorkout(1)

        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assert(uiState is EditWorkoutUiState.EditWorkoutLoaded)
    }

    @Test
    fun `onNameChange should update name in uiState`() = runTest {
        viewModel.getWorkout(1)
        advanceUntilIdle()
        viewModel.onNameChange("New Workout Name")

        val uiState = viewModel.uiState.value
        assert(uiState is EditWorkoutUiState.EditWorkoutLoaded)
        assertThat((uiState as EditWorkoutUiState.EditWorkoutLoaded).name).isEqualTo("New Workout Name")
    }

    @Test
    fun `getWorkoutExercises should populate exercises list`() {
        viewModel.getWorkoutExercises()

        assertThat(viewModel.exercises).containsExactlyElementsIn(exercises)
    }

    @Test
    fun `removeButtonOnClick should remove exercise from lists`() {
        val exerciseToRemove = exercises[0]
        viewModel.removeButtonOnClick(exerciseToRemove)

        assertThat(viewModel.exercises).doesNotContain(exerciseToRemove)
    }

    @Test
    fun `saveButtonOnClick should update workout successfully`() = runTest {
        viewModel.getWorkout(1)
        advanceUntilIdle()
        viewModel.getWorkoutExercises()
        viewModel.saveButtonOnClick(1)
        advanceUntilIdle()
        coVerify { mockPresenter.updateWorkout(any()) }
        val uiState = viewModel.uiState.value
        assert(uiState is EditWorkoutUiState.EditWorkoutSaved)
    }

    @Test
    fun `saveButtonOnClick should delete workout if no exercises present`() = runTest {
        coEvery { mockPresenter.getWorkout(any()) } returns Workout()
        viewModel.getWorkout(1)
        advanceUntilIdle()
        viewModel.saveButtonOnClick(1)
        advanceUntilIdle()
        coVerify { mockPresenter.deleteWorkout(1) }
        val uiState = viewModel.uiState.first()
        assert(uiState is EditWorkoutUiState.EditWorkoutSaved)
    }

    @Test
    fun `saveButtonOnClick should handle update failure`() = runTest {
        val exception = Exception("Update failed")
        coEvery { mockPresenter.updateWorkout(any()) } throws exception

        viewModel.getWorkout(1)
        advanceUntilIdle()
        viewModel.getWorkoutExercises()
        viewModel.saveButtonOnClick(1)
        advanceUntilIdle()
        val failureEvent = viewModel.updateWorkoutFailedEvent.value
        assertThat(failureEvent.isUpdateWorkoutFailed).isTrue()
        assertThat(failureEvent.exception).isEqualTo(exception)
    }

    @Test
    fun `handledUpdateWorkoutFailedEvent should reset failure state`() {

        viewModel.handledUpdateWorkoutFailedEvent()

        assertThat(viewModel.updateWorkoutFailedEvent.value.isUpdateWorkoutFailed).isFalse()
    }
}
