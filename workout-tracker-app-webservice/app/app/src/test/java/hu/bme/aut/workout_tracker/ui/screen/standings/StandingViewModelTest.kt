package hu.bme.aut.workout_tracker.ui.screen.standings

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
class StandingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StandingViewModel
    private val mockPresenter = mockk<WorkoutTrackerPresenter>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val currentUser = User(email = "test@example.com", firstName = "John", lastName = "Doe", photo = byteArrayOf())
    private val charts = listOf(
        Chart(
            id = 1,
            type = ChartType.OneRepMax,
            userId = "test@example.com",
            exercise = Exercise(id = 1, category = "Legs", name = "Squat"),
            data = mutableListOf(100.0)
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StandingViewModel(mockPresenter)
        coEvery { mockPresenter.getCurrentUser() } returns currentUser
        coEvery { mockPresenter.getCharts() } returns charts
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should update uiState, exercises and users`() = runTest {
        val exercises = listOf(Exercise(id = 1, category = "Legs", name = "Squat"))
        val users = listOf(User(email = "user@example.com", firstName = "Jane", lastName = "Doe", photo = byteArrayOf()))

        coEvery { mockPresenter.getStandingsExercises() } returns exercises
        coEvery { mockPresenter.getUsers() } returns users

        viewModel.init()

        advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assert(uiState is StandingUiState.StandingLoaded)
        assertThat(viewModel.exercises.value).isEqualTo(exercises)
        assertThat(viewModel.users.value).isEqualTo(users)
    }

    @Test
    fun `onSelectedItemChangeByName should update selected item in uiState`() {
        viewModel.init()

        val exercises = listOf(Exercise(id = 1, category = "Legs", name = "Squat"))
        viewModel.onSelectedItemChangeByName("Squat", exercises)

        val uiState = viewModel.uiState.value
        assert(uiState is StandingUiState.StandingLoaded)
        val loadedState = uiState as StandingUiState.StandingLoaded
        assertThat(loadedState.selectedItem.name).isEqualTo("Squat")
    }

    @Test
    fun `getBestUsers should return correct mapping of users and max data`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        val users = listOf(currentUser)
        val bestUsers = viewModel.getBestUsers(users, 1)

        assertThat(bestUsers.size).isEqualTo(1)
        assertThat(bestUsers[currentUser]).isEqualTo(100.0)
    }

    @Test
    fun `isCurrentUser should return true for the current user`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        val result = viewModel.isCurrentUser(currentUser)
        assertThat(result).isTrue()
    }

    @Test
    fun `isCurrentUser should return false for a different user`() {
        val otherUser = User(email = "other@example.com", firstName = "Jane", lastName = "Smith", photo = byteArrayOf())
        val result = viewModel.isCurrentUser(otherUser)
        assertThat(result).isFalse()
    }
}
