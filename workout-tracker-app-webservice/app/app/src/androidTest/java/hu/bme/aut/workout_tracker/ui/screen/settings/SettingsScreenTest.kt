package hu.bme.aut.workout_tracker.ui.screen.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navigateBack: () -> Unit
    private lateinit var signOut: () -> Unit
    private lateinit var viewModel: SettingsViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    @Before
    fun setup() {
        workoutTrackerPresenter = mockk()
        navigateBack = mockk(relaxed = true)
        signOut = mockk(relaxed = true)
        viewModel = SettingsViewModel(workoutTrackerPresenter)

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns User()
    }

    @Test
    fun testSettingsScreenDisplaysUIComponents() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                SettingsScreen(
                    navigateBack = navigateBack,
                    signOut = signOut,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.settings))
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Logout Icon").assertIsDisplayed()

        composeTestRule.onNodeWithText("First Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Last Name").assertIsDisplayed()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
            .assertIsDisplayed()
    }

    @Test
    fun testClickLogoutIconTriggersSignOut() {
        coEvery { workoutTrackerPresenter.signOut() } just runs

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                SettingsScreen(
                    navigateBack = navigateBack,
                    signOut = signOut,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Logout Icon").performClick()

        verify { signOut.invoke() }
    }

    @Test
    fun testClickSaveButtonTriggersSaveAction() {
        coEvery { workoutTrackerPresenter.updateUser(any(), any()) } answers {
            val onSuccess = secondArg<() -> Unit>()
            onSuccess()
        }

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                SettingsScreen(
                    navigateBack = navigateBack,
                    signOut = signOut,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("First Name").performTextInput("Test")
        composeTestRule.onNodeWithText("Last Name").performTextInput("User")

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
            .performClick()

        assertThat(viewModel.uiState.value).isEqualTo(SettingsUiState.SettingsSaved)

    }

    @Test
    fun testErrorDialogIsDisplayedOnUpdateUserFailure() {
        coEvery { workoutTrackerPresenter.updateUser(any(), any()) } throws Exception()

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                SettingsScreen(
                    navigateBack = navigateBack,
                    signOut = signOut,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("First Name").performTextInput("Test")
        composeTestRule.onNodeWithText("Last Name").performTextInput("User")

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.update_profile_failed))
            .assertIsDisplayed()
    }
}
