import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.flow.first
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class YourWorkoutsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: YourWorkoutsViewModel
    private val mockPresenter: WorkoutTrackerPresenter = mockk()
    private val testUser = User(
        email = "test@example.com",
        firstName = "",
        lastName = "",
        photo = byteArrayOf()
    )
    private val testWorkouts = listOf(
        Workout(), Workout(1, "1", "Morning Workout", true, mutableListOf())
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockPresenter.getCurrentUser() } returns testUser
        coEvery { mockPresenter.getUserWorkouts(any()) } returns testWorkouts
        coEvery { mockPresenter.updateWorkout(any()) } returns Unit

        viewModel = YourWorkoutsViewModel(mockPresenter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWorkouts should update uiState and workouts`() = runTest {
        viewModel.getWorkouts()
        advanceUntilIdle()
        val uiState = viewModel.uiState.value
        assertThat(uiState).isInstanceOf(YourWorkoutsUiState.YourWorkoutsLoaded::class.java)

        coVerify { mockPresenter.getCurrentUser() }
        coVerify { mockPresenter.getUserWorkouts(any()) }
    }

    @Test
    fun `isFavoriteOnClick should update workout and call presenter`() = runTest {
        val workout = Workout(1, "1", "Morning Workout", true, mutableListOf())

        viewModel.isFavoriteOnClick(workout)
        advanceUntilIdle()
        assertThat(workout.isFavorite).isFalse()
        coVerify { mockPresenter.updateWorkout(workout) }
    }
}
