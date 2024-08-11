import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInUiState
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInViewModel
import hu.bme.aut.workout_tracker.utils.Constants
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SignInViewModelTest {

    private lateinit var viewModel: SignInViewModel
    private val mockPresenter = mockk<WorkoutTrackerPresenter>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataStore: DataStore<Preferences>


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignInViewModel(mockPresenter)
        dataStore = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChange should update uiState correctly`() {
        viewModel.changeUiStateToSignInLoaded()
        viewModel.onEmailChange("test@example.com")

        val state = viewModel.uiState.value as SignInUiState.SignInLoaded
        assertThat(state.email).isEqualTo("test@example.com")
    }

    @Test
    fun `onPasswordChange should update uiState correctly`() {
        viewModel.changeUiStateToSignInLoaded()
        viewModel.onPasswordChange("password")

        val state = viewModel.uiState.value as SignInUiState.SignInLoaded
        assertThat(state.password).isEqualTo("password")
    }

    @Test
    fun `isButtonEnabled should return true when email and password are valid`() {
        viewModel.changeUiStateToSignInLoaded()
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password")

        assertThat(viewModel.isButtonEnabled()).isTrue()
    }

    @Test
    fun `isButtonEnabled should return false when email or password is invalid`() {
        viewModel.changeUiStateToSignInLoaded()
        viewModel.onEmailChange("invalid-email")
        viewModel.onPasswordChange("password")

        assertThat(viewModel.isButtonEnabled()).isFalse()

        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("")

        assertThat(viewModel.isButtonEnabled()).isFalse()
    }

    @Test
    fun `isLoggedIn should return true when token and currentUserEmail are not empty`() {
        Constants.token = "Bearer test-token"
        Constants.currentUserEmail = "test@example.com"

        assertThat(viewModel.isLoggedIn()).isTrue()
    }

    @Test
    fun `isLoggedIn should return false when token or currentUserEmail are empty`() {
        Constants.token = ""
        Constants.currentUserEmail = "test@example.com"

        assertThat(viewModel.isLoggedIn()).isFalse()

        Constants.token = "Bearer test-token"
        Constants.currentUserEmail = ""

        assertThat(viewModel.isLoggedIn()).isFalse()
    }

    @Test
    fun `buttonOnClick should update user and uiState on success`() = runTest {
        viewModel.changeUiStateToSignInLoaded()
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password")
        coEvery { mockPresenter.signIn(any(), any(), any()) } answers {
            val onSuccess = secondArg<(String) -> Unit>()
            onSuccess("test-token")
        }

        viewModel.buttonOnClick(dataStore)

        advanceUntilIdle()

        assertThat(viewModel.uiState.value).isInstanceOf(SignInUiState.SignInSuccess::class.java)
        assertThat(viewModel.savingState.value).isTrue()
    }

    @Test
    fun `buttonOnClick should update failure event on exception`() = runTest {
        val dataStore = mockk<DataStore<Preferences>>()
        viewModel.changeUiStateToSignInLoaded()
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password")
        coEvery { mockPresenter.signIn(any(), any(), any()) } answers {
            val onFailure = thirdArg<(Exception) -> Unit>()
            onFailure(Exception("Test Exception"))
        }

        viewModel.buttonOnClick(dataStore)

        advanceUntilIdle()

        assertThat(viewModel.savingState.value).isFalse()
        assertThat(viewModel.signInFailedEvent.value.isLoginFailed).isTrue()
    }

    @Test
    fun `handledSignInFailedEvent should reset failed event`() {
        viewModel.changeUiStateToSignInLoaded()

        viewModel.handledSignInFailedEvent()

        val state = viewModel.uiState.value as SignInUiState.SignInLoaded
        assertThat(state.email).isEmpty()
        assertThat(state.password).isEmpty()
        assertThat(viewModel.signInFailedEvent.value.isLoginFailed).isFalse()
    }
}
