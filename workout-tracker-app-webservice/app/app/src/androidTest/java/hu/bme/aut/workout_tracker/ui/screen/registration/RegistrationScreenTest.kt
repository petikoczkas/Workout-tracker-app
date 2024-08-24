package hu.bme.aut.workout_tracker.ui.screen.registration

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var navigateToRegistrationSuccess: () -> Unit
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        workoutTrackerPresenter = mockk()
        viewModel = RegistrationViewModel(workoutTrackerPresenter)
        navigateToRegistrationSuccess = mockk(relaxed = true)

        coEvery { workoutTrackerPresenter.registrate(any(), any(), any()) } just runs
    }

    @Test
    fun testRegistrationScreenDisplaysUIComponents() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                RegistrationScreen(
                    navigateToRegistrationSuccess = navigateToRegistrationSuccess,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.registration)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.first_name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.last_name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.registrate)).assertIsDisplayed()
    }

    @Test
    fun testButtonClickTriggersRegistration() {
        coEvery { workoutTrackerPresenter.registrate(any(), any(), any()) } answers {
            val onSuccess = secondArg<(String) -> Unit>()
            onSuccess("token")
        }
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                RegistrationScreen(
                    navigateToRegistrationSuccess = navigateToRegistrationSuccess,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Email").performTextInput("test@test.com")
        composeTestRule.onNodeWithText("First Name").performTextInput("Test")
        composeTestRule.onNodeWithText("Last Name").performTextInput("User")
        composeTestRule.onNodeWithText("Password").performTextInput("123456Aa")
        composeTestRule.onNodeWithText("Password Again").performTextInput("123456Aa")

        composeTestRule.onNodeWithText(context.getString(R.string.registrate)).performClick()
        assertThat(viewModel.uiState.value).isEqualTo(RegistrationUiState.RegistrationSuccess)
    }

    @Test
    fun testShowRegistrationFailedDialog() {
        coEvery { workoutTrackerPresenter.registrate(any(), any(), any()) } answers {
            val onFailure = thirdArg<(Exception) -> Unit>()
            onFailure(Exception())
        }


        composeTestRule.setContent {
            WorkoutTrackerTheme {
                RegistrationScreen(
                    navigateToRegistrationSuccess = navigateToRegistrationSuccess,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Email").performTextInput("test@test.com")
        composeTestRule.onNodeWithText("First Name").performTextInput("Test")
        composeTestRule.onNodeWithText("Last Name").performTextInput("User")
        composeTestRule.onNodeWithText("Password").performTextInput("123456Aa")
        composeTestRule.onNodeWithText("Password Again").performTextInput("123456Aa")

        composeTestRule.onNodeWithText(context.getString(R.string.registrate)).performClick()

        composeTestRule.onNodeWithText(context.getString(R.string.registration_failed))
            .assertIsDisplayed()
    }
}
