package hu.bme.aut.workout_tracker.ui.screen.registration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        workoutTrackerPresenter = mockk(relaxed = true)
        dataStore = mockk(relaxed = true)
        viewModel = RegistrationViewModel(workoutTrackerPresenter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test onEmailChange updates email correctly`() = runTest {
        viewModel.onEmailChange("test@example.com")

        val uiState = viewModel.uiState.value as RegistrationUiState.RegistrationLoaded
        assertThat(uiState.email).isEqualTo("test@example.com")
    }

    @Test
    fun `test buttonOnClick calls registration and updates state on success`() = runTest {
        val token = "test_token"

        coEvery { workoutTrackerPresenter.registrate(any(), any(), any()) } answers {
            val onSuccess = secondArg<(String) -> Unit>()
            onSuccess(token)
        }

        viewModel.onEmailChange("test@example.com")
        viewModel.onFirstNameChange("First")
        viewModel.onLastNameChange("Last")
        viewModel.onPasswordChange("Password123")
        viewModel.onPasswordAgainChange("Password123")

        viewModel.buttonOnClick(dataStore)

        assertThat(viewModel.uiState.value).isEqualTo(RegistrationUiState.RegistrationSuccess)
    }

    @Test
    fun `test buttonOnClick updates registration failed event on failure`() = runTest {
        val exception = Exception("Registration failed")

        coEvery { workoutTrackerPresenter.registrate(any(), any(), any()) } answers {
            val onFailure = thirdArg<(Exception) -> Unit>()
            onFailure(exception)
        }

        viewModel.onEmailChange("test@example.com")
        viewModel.onFirstNameChange("First")
        viewModel.onLastNameChange("Last")
        viewModel.onPasswordChange("Password123")
        viewModel.onPasswordAgainChange("Password123")

        viewModel.buttonOnClick(dataStore)

        val registrationFailedEvent = viewModel.registrationFailedEvent.value
        assertThat(registrationFailedEvent.isRegistrationFailed).isTrue()
        assertThat(registrationFailedEvent.exception).isEqualTo(exception)
        assertThat(viewModel.savingState.value).isFalse()
    }

    @Test
    fun `test isButtonEnabled returns correct value`() = runTest {
        viewModel.onEmailChange("test@example.com")
        viewModel.onFirstNameChange("First")
        viewModel.onLastNameChange("Last")
        viewModel.onPasswordChange("Password123")
        viewModel.onPasswordAgainChange("Password123")

        assertThat(viewModel.isButtonEnabled()).isTrue()

        viewModel.onEmailChange("invalid_email")
        assertThat(viewModel.isButtonEnabled()).isFalse()
    }
}
