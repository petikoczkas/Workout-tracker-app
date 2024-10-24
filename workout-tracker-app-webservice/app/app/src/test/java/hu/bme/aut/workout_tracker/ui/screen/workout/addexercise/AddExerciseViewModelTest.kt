package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AddExerciseViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddExerciseViewModel
    private val mockPresenter = mockk<WorkoutTrackerPresenter>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val mockContext = mockk<Context>(relaxed = true)

    private val exercises = listOf(
        Exercise(id = 1, category = "Legs", name = "Squat"),
        Exercise(id = 2, category = "Arms", name = "Curl")
    )
    private val workout = Workout(id = 1, userId = "", name = "Test Workout", isFavorite = false, exercises = exercises.toMutableList())


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddExerciseViewModel(mockPresenter)
        coEvery { mockPresenter.getWorkout(any()) } returns workout
        coEvery { mockPresenter.getExercises() } returns exercises
        mockkStatic("hu.bme.aut.workout_tracker.utils.AppData")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic("hu.bme.aut.workout_tracker.utils.AppData")
    }

    @Test
    fun `getExercises should update uiState and exercises`() = runTest {
        viewModel.getExercises(1)

        advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assert(uiState is AddExerciseUiState.AddExerciseLoaded)
        assertThat(viewModel.exercises.value).isEqualTo(exercises)
    }

    @Test
    fun `onSelectedItemChange should update selectedItem in uiState`() {
        viewModel.getExercises(1)

        viewModel.onSelectedItemChange("Arms")

        val uiState = viewModel.uiState.value
        assert(uiState is AddExerciseUiState.AddExerciseLoaded)
        assertThat((uiState as AddExerciseUiState.AddExerciseLoaded).selectedItem).isEqualTo("Arms")
    }

    @Test
    fun `getExercisesByCategory should return filtered exercises`() = runTest {
        viewModel.getExercises(1)
        advanceUntilIdle()
        viewModel.onSelectedItemChange("Legs")
        val filteredExercises = viewModel.getExercisesByCategory(exercises)

        assertThat(filteredExercises).containsExactly(
            Exercise(id = 1, category = "Legs", name = "Squat")
        )
    }

    @Test
    fun `dialogSaveButtonOnClick should handle new exercise correctly`() = runTest {
        viewModel.getExercises(1)
        val newExerciseName = "Deadlift"
        viewModel.onNewExerciseChange(newExerciseName)
        every { mockContext.getString(R.string.exercise_already_exists) } returns "Exercise already exists"

        viewModel.dialogSaveButtonOnClick(exercises, mockContext)

        advanceUntilIdle()

        coVerify { mockPresenter.createExercise(any()) }
        assertThat(viewModel.createExerciseFailedEvent.first().isCreateExerciseFailed).isFalse()
    }

    @Test
    fun `dialogSaveButtonOnClick should handle existing exercise correctly`() {
        val existingExerciseName = "Squat"
        every { mockContext.getString(R.string.exercise_already_exists) } returns "Exercise already exists"

        viewModel.getExercises(1)
        viewModel.onNewExerciseChange(existingExerciseName)

        viewModel.dialogSaveButtonOnClick(exercises, mockContext)

        assertThat(viewModel.createExerciseFailedEvent.value.isCreateExerciseFailed).isTrue()
    }

    @Test
    fun `dialogSaveButtonOnClick should handle creation failure`() = runTest {
        val newExerciseName = "Deadlift"
        every { mockContext.getString(R.string.exercise_already_exists) } returns "Exercise already exists"
        coEvery { mockPresenter.createExercise(any()) } throws Exception("Creation failed")

        viewModel.getExercises(1)
        viewModel.onNewExerciseChange(newExerciseName)

        viewModel.dialogSaveButtonOnClick(exercises, mockContext)

        advanceUntilIdle()

        assertThat(viewModel.createExerciseFailedEvent.first().isCreateExerciseFailed).isTrue()
        assertThat(viewModel.createExerciseFailedEvent.first().exception?.message).isEqualTo("Creation failed")
    }

    @Test
    fun `handledCreateExerciseFailedEvent should reset failure state`() {

        viewModel.handledCreateExerciseFailedEvent()

        assertThat(viewModel.createExerciseFailedEvent.value.isCreateExerciseFailed).isFalse()
    }
}
