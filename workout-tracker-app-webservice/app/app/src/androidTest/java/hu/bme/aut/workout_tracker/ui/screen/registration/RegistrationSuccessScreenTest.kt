package hu.bme.aut.workout_tracker.ui.screen.registration

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationSuccessScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navigateToHome: () -> Unit

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        navigateToHome = mockk(relaxed = true)
    }

    @Test
    fun testRegistrationSuccessScreenDisplaysUIComponents() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                RegistrationSuccessScreen(navigateToHome = navigateToHome)
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.successful_registration))
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("SuccessfulRegistrationAnimation").assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.next)).assertIsDisplayed()
    }

    @Test
    fun testClickNextNavigatesToHome() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                RegistrationSuccessScreen(navigateToHome = navigateToHome)
            }
        }
        composeTestRule.onNodeWithText(context.getString(R.string.next)).performClick()

        verify { navigateToHome() }
    }
}
