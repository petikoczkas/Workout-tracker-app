package hu.bme.aut.workout_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        workoutTrackerPresenter = mockk(relaxed = true)
        viewModel = MainViewModel(workoutTrackerPresenter)
    }

    @Test
    fun `updateServerAvailability should set isServerAvailable to true on success`() = runBlockingTest(testDispatcher) {
        every { workoutTrackerPresenter.isAvailable(any(), any()) } answers {
            firstArg<() -> Unit>().invoke()
        }

        viewModel.updateServerAvailability()
        advanceUntilIdle()

        assertThat(viewModel.isServerAvailable.first()).isTrue()
    }

    @Test
    fun `updateServerAvailability should set isServerAvailable to false on failure`() = runBlockingTest(testDispatcher) {
        every { workoutTrackerPresenter.isAvailable(any(), any()) } answers {
            secondArg<() -> Unit>().invoke()
        }

        viewModel.updateServerAvailability()
        advanceUntilIdle()

        assertThat(viewModel.isServerAvailable.first()).isFalse()
    }
}
