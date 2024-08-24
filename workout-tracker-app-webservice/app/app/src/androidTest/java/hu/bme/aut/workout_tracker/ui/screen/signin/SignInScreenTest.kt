package hu.bme.aut.workout_tracker.ui.screen.signin

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: SignInViewModel
    private lateinit var navigateToHome: () -> Unit
    private lateinit var navigateToRegistration: () -> Unit
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    @Before
    fun setup() {
        workoutTrackerPresenter = mockk()
        viewModel = SignInViewModel(workoutTrackerPresenter)
        navigateToHome = mockk(relaxed = true)
        navigateToRegistration = mockk(relaxed = true)
        viewModel.changeUiStateToSignInLoaded()
    }

    @Test
    fun testSignInLoadedStateDisplaysUIComponents() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                SignInScreen(
                    navigateToHome = navigateToHome,
                    navigateToRegistration = navigateToRegistration,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onAllNodesWithText(composeTestRule.activity.getString(R.string.sign_in))
            .assertCountEquals(2)

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.registrate))
            .assertIsDisplayed()
    }

    @Test
    fun testClickSignInButtonTriggersLoadingDialog() {
        coEvery { workoutTrackerPresenter.signIn(any(), any(), any()) } answers {
            val onSuccess = secondArg<(String) -> Unit>()
            onSuccess("token")
        }

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                SignInScreen(
                    navigateToHome = navigateToHome,
                    navigateToRegistration = navigateToRegistration,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
            .performTextInput("test@test.com")

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
            .performTextInput("test")

        composeTestRule.onAllNodesWithText(composeTestRule.activity.getString(R.string.sign_in))[1]
            .performClick()

        assertThat(viewModel.uiState.value).isEqualTo(SignInUiState.SignInSuccess)
    }

    @Test
    fun testSignInFailureDisplaysErrorDialog() {
        coEvery { workoutTrackerPresenter.signIn(any(), any(), any()) } answers {
            val onSuccess = thirdArg<(Exception) -> Unit>()
            onSuccess(Exception())
        }

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                SignInScreen(
                    navigateToHome = navigateToHome,
                    navigateToRegistration = navigateToRegistration,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
            .performTextInput("test@test.com")

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
            .performTextInput("test")

        composeTestRule.onAllNodesWithText(composeTestRule.activity.getString(R.string.sign_in))[1]
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.login_failed))
            .assertIsDisplayed()
    }
}
