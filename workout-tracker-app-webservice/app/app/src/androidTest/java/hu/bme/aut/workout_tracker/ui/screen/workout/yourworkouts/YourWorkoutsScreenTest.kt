package hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class YourWorkoutsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: YourWorkoutsViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    @Before
    fun setup() {
        workoutTrackerPresenter = mockk()
        viewModel = YourWorkoutsViewModel(workoutTrackerPresenter)

        val exercise = Exercise(id = 1, category = "Legs", name = "Squat")
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
        coEvery { workoutTrackerPresenter.getUserWorkouts(any()) } returns listOf(workout)

    }

    @Test
    fun yourWorkoutsScreenDisplaysNoWorkoutsTextWhenWorkoutsIsEmpty() {
        coEvery { workoutTrackerPresenter.getUserWorkouts(any()) } returns listOf()

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                YourWorkoutsScreen(
                    navigateToWorkout = {},
                    navigateToEditWorkout = {},
                    navigateToCreateWorkout = {},
                    viewModel = viewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.you_have_no_workouts))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun yourWorkoutsScreenDisplaysWorkoutCardsWhenWorkoutsIsNotEmpty() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                YourWorkoutsScreen(
                    navigateToWorkout = {},
                    navigateToEditWorkout = {},
                    navigateToCreateWorkout = {},
                    viewModel = viewModel

                )
            }
        }

        composeTestRule.onNodeWithText("Test workout").assertIsDisplayed()
    }

    @Test
    fun yourWorkoutsScreenNavigatesOnButtonClick() {
        val navigateToCreateWorkout = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                YourWorkoutsScreen(
                    navigateToWorkout = {},
                    navigateToEditWorkout = {},
                    navigateToCreateWorkout = navigateToCreateWorkout,
                    viewModel = viewModel

                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.create_workout))
            .performClick()
        verify { navigateToCreateWorkout() }
    }
}
