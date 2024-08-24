package hu.bme.aut.workout_tracker.ui.screen.workout.workout

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.SavedExercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import hu.bme.aut.workout_tracker.utils.Constants.currentUserEmail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    private val exercise = Exercise(id = 1, category = "Legs", name = "Squat")

    @Before
    fun setUp() {
        workoutTrackerPresenter = mockk()
        viewModel = WorkoutViewModel(workoutTrackerPresenter)

        val workout = Workout(
            id = 1,
            userId = "test@test.com",
            name = "Test workout",
            isFavorite = true,
            exercises = mutableListOf(exercise)
        )
        val user = User(
            email = "test@test.com",
            firstName = "John",
            lastName = "Doe",
            photo = byteArrayOf()
        )

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns user
        coEvery { workoutTrackerPresenter.getWorkout(any()) } returns workout
        coEvery { workoutTrackerPresenter.getUserSavedExercises(any()) } returns mutableListOf()
        coEvery { workoutTrackerPresenter.getUserCharts(any()) } returns mutableListOf()

    }

    @Test
    fun testInitialStateCalls() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                WorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        coVerify(exactly = 1) { workoutTrackerPresenter.getWorkout(1) }
        coVerify(exactly = 1) { workoutTrackerPresenter.getCurrentUser() }
        coVerify(exactly = 1) { workoutTrackerPresenter.getUserSavedExercises(currentUserEmail) }
        coVerify(exactly = 1) { workoutTrackerPresenter.getUserCharts(currentUserEmail) }
    }

    @Test
    fun testWorkoutScreenDisplaysContentWhenLoaded() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                WorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Squat").assertExists()
        composeTestRule.onNodeWithText("Switch Exercise").assertExists()
        composeTestRule.onNodeWithText("Save").assertExists()
    }

    @Test
    fun testPlusAndMinusIconButton() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                WorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Add").performClick()

        composeTestRule.onNodeWithText("2.").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Remove").performClick()

        composeTestRule.onNodeWithText("2.").assertDoesNotExist()
    }

    @Test
    fun testWorkoutScreenTriggersSaveButtonOnClick() {
        coEvery { workoutTrackerPresenter.getUserSavedExercises(any()) } returns mutableListOf(
            SavedExercise(
                id = 1,
                userId = "test@test.com",
                exercise = exercise,
                data = mutableListOf("100 10", "100 10")
            )
        )

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                WorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText("Workout Complete").assertIsDisplayed()
    }
}
