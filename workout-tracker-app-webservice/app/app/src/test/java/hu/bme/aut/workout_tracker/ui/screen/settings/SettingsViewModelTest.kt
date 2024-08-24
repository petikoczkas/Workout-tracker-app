import android.content.ContentResolver
import android.net.Uri
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsViewModel
import hu.bme.aut.workout_tracker.utils.getByteArray
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private val mockPresenter = mockk<WorkoutTrackerPresenter>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingsViewModel(mockPresenter)

        val mockUser = User(email = "", firstName = "John", lastName = "Doe", photo = byteArrayOf())
        coEvery { mockPresenter.getCurrentUser() } returns mockUser

        viewModel.getCurrentUser()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentUser should update uiState correctly`() = runTest {
        assertThat(viewModel.uiState.value).isInstanceOf(SettingsUiState.SettingsLoaded::class.java)
        val state = viewModel.uiState.value as SettingsUiState.SettingsLoaded
        assertThat(state.firstName).isEqualTo("John")
        assertThat(state.lastName).isEqualTo("Doe")
    }

    @Test
    fun `onFirstNameChange should update uiState correctly`() {
        viewModel.onFirstNameChange("Jane")

        assertThat((viewModel.uiState.value as SettingsUiState.SettingsLoaded).firstName).isEqualTo("Jane")
    }

    @Test
    fun `onLastNameChange should update uiState correctly`() {

        viewModel.onLastNameChange("Smith")

        assertThat((viewModel.uiState.value as SettingsUiState.SettingsLoaded).lastName).isEqualTo("Smith")
    }

    @Test
    fun `onImageChange should update uiState correctly`() {
        val uri = Uri.parse("content://path/to/image")
        viewModel.onImageChange(uri)

        assertThat((viewModel.uiState.value as SettingsUiState.SettingsLoaded).imageUri).isEqualTo(uri)
    }

    @Test
    fun `isSaveButtonEnabled should return true when firstName and lastName are not empty`() {
        viewModel.onFirstNameChange("Test")
        viewModel.onLastNameChange("User")
        assertThat(viewModel.isSaveButtonEnabled()).isTrue()
    }

    @Test
    fun `isSaveButtonEnabled should return false when firstName or lastName is empty`() {
        viewModel.onLastNameChange("")
        assertThat(viewModel.isSaveButtonEnabled()).isFalse()
    }

    @Test
    fun `saveButtonOnClick should update user and uiState on success`() = runTest {
        val contentResolver = mockk<ContentResolver>()
        val imageUri = Uri.parse("content://path/to/image")
        val inputStream = mockk<java.io.InputStream>(relaxed = true)
        coEvery { contentResolver.openInputStream(imageUri) } returns inputStream
        coEvery { inputStream.read(any(), any(), any()) } returns -1
        coEvery { mockPresenter.updateUser(any(), any()) } answers {
            val onSuccess = secondArg<() -> Unit>()
            onSuccess()
        }

        viewModel.onImageChange(imageUri)

        viewModel.saveButtonOnClick(contentResolver)

        advanceUntilIdle()

        assertThat(viewModel.uiState.value).isInstanceOf(SettingsUiState.SettingsSaved::class.java)
        assertThat(viewModel.savingState.value).isTrue()
    }

    @Test
    fun `saveButtonOnClick should update failure event on exception`() = runTest {
        val contentResolver = mockk<ContentResolver>()
        coEvery { contentResolver.openInputStream(any()) } throws Exception("Test Exception")
        coEvery { mockPresenter.updateUser(any(), any()) } throws Exception("Test Exception")

        viewModel.saveButtonOnClick(contentResolver)

        advanceUntilIdle()

        assertThat(viewModel.savingState.value).isFalse()
        assertThat(viewModel.updateUserFailedEvent.value.isUpdateUserFailed).isTrue()
    }
}
