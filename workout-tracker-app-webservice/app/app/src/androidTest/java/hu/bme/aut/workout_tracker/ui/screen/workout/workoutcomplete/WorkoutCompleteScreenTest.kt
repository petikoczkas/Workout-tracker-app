package hu.bme.aut.workout_tracker.ui.screen.workout.workoutcomplete

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutCompleteScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun workoutCompleteScreenDisplaysTextAndButtons() {
        val navigateToAddExercise = mockk<() -> Unit>(relaxed = true)
        val navigateBack = mockk<() -> Unit>(relaxed = true)
        val focusManager = mockk<androidx.compose.ui.focus.FocusManager>(relaxed = true)

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                WorkoutCompleteScreen(
                    focusManager = focusManager,
                    navigateToAddExercise = navigateToAddExercise,
                    navigateBack = navigateBack
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.workout_complete))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_exercise))
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.end_workout))
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun workoutCompleteScreenNavigatesOnButtonClick() {
        val navigateToAddExercise = mockk<() -> Unit>(relaxed = true)
        val navigateBack = mockk<() -> Unit>(relaxed = true)
        val focusManager = mockk<androidx.compose.ui.focus.FocusManager>(relaxed = true)

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                WorkoutCompleteScreen(
                    focusManager = focusManager,
                    navigateToAddExercise = navigateToAddExercise,
                    navigateBack = navigateBack
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_exercise))
            .performClick()
        verify { navigateToAddExercise() }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.end_workout))
            .performClick()
        verify { navigateBack() }
    }
}
