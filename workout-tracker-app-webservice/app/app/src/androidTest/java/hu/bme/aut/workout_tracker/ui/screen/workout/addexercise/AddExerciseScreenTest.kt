package hu.bme.aut.workout_tracker.ui.screen.workout.addexercise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddExerciseScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: AddExerciseViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter


    @Before
    fun setup() {
        workoutTrackerPresenter = mockk()
        viewModel = AddExerciseViewModel(workoutTrackerPresenter)

        val exercise1 = Exercise(id = 1, category = "Legs", name = "Squat")
        val exercise2 = Exercise(id = 2, category = "Biceps", name = "Curl")
        val workout = Workout(
            id = 1,
            userId = "0",
            name = "Workout 1",
            isFavorite = true,
            exercises = mutableListOf(exercise1)
        )

        coEvery { workoutTrackerPresenter.getWorkout(any()) } returns workout
        coEvery { workoutTrackerPresenter.getExercises() } returns listOf(exercise1, exercise2)
    }

    @Test
    fun testInitialStateCalls() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                AddExerciseScreen(workoutId = 1, navigateBack = {}, viewModel = viewModel)
            }
        }

        coVerify(exactly = 1) { workoutTrackerPresenter.getWorkout(1) }
        coVerify(exactly = 1) { workoutTrackerPresenter.getExercises() }
    }

    @Test
    fun testLoadedStateDisplaysTitleAndDropDownMenu() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                AddExerciseScreen(workoutId = 1, navigateBack = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_exercise))
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("WorkoutTrackerDropDownMenu").assertIsDisplayed()
    }

    @Test
    fun testLoadedStateWithExercisesDisplaysExerciseCards() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                AddExerciseScreen(workoutId = 1, navigateBack = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithTag("WorkoutTrackerDropDownMenu").performClick()
        composeTestRule.onNodeWithText("Biceps").performClick()

        composeTestRule.onNodeWithText("Curl").assertIsDisplayed()
    }

    @Test
    fun testCreateExerciseDialogShowsUpWhenButtonClicked() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                AddExerciseScreen(workoutId = 1, navigateBack = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.create_a_custom_exercise))
            .performClick()

        composeTestRule.onNodeWithTag("AddExerciseDialog").assertIsDisplayed()
    }

    @Test
    fun testErrorDialogIsShownOnCreateExerciseFailure() {
        coEvery { workoutTrackerPresenter.createExercise(any()) } throws Exception()

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                AddExerciseScreen(workoutId = 1, navigateBack = {}, viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.create_a_custom_exercise))
            .performClick()
        composeTestRule.onNodeWithText("Name").performTextInput("Push up")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.create_exercise_failed))
            .assertIsDisplayed()
    }
}
