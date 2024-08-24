package hu.bme.aut.workout_tracker.ui.screen.workout.editworkout

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
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
class EditWorkoutScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: EditWorkoutViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter

    @Before
    fun setup() {
        workoutTrackerPresenter = mockk()
        viewModel = EditWorkoutViewModel(workoutTrackerPresenter)

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
        coEvery { workoutTrackerPresenter.getWorkout(any()) } returns workout

    }

    @Test
    fun testInitialStateCalls() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                EditWorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        coVerify(exactly = 1) { workoutTrackerPresenter.getCurrentUser() }
        coVerify(exactly = 1) { workoutTrackerPresenter.getWorkout(1) }
    }

    @Test
    fun testLoadedStateDisplaysWorkoutNameAndExercises() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                EditWorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Test workout").assertIsDisplayed()
        composeTestRule.onNodeWithText("Squat").assertIsDisplayed()
    }

    @Test
    fun testLoadedStateWithNoExercisesDisplaysNoExercisesText() {
        val emptyWorkout = Workout(
            id = 1,
            userId = "test@test.com",
            name = "Test workout",
            isFavorite = true,
            exercises = mutableListOf()
        )
        coEvery { workoutTrackerPresenter.getWorkout(any()) } returns emptyWorkout

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                EditWorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.no_exercises))
            .assertIsDisplayed()
    }

    @Test
    fun testSaveButtonEnabledOnlyWhenWorkoutNameIsNotBlank() {
        val workout = Workout(
            id = 1,
            userId = "test@test.com",
            name = "",
            isFavorite = true,
            exercises = mutableListOf()
        )
        coEvery { workoutTrackerPresenter.getWorkout(any()) } returns workout

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                EditWorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
            .assertIsNotEnabled()

        composeTestRule.onNodeWithText("Workout Name").performTextInput("Test Workout")

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
            .assertIsEnabled()
    }

    @Test
    fun testAddExerciseButtonNavigatesToAddExerciseScreen() {
        var navigateCalled = false

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                EditWorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = { navigateCalled = true },
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add_exercise))
            .performClick()

        assert(navigateCalled)
    }

    @Test
    fun testErrorDialogIsShownOnUpdateWorkoutFailure() {
        coEvery { workoutTrackerPresenter.updateWorkout(any()) } throws Exception()

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                EditWorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.workout_update_failed))
            .assertIsDisplayed()
    }

    @Test
    fun testNavigationBackOnWorkoutSaved() {
        coEvery { workoutTrackerPresenter.updateWorkout(any()) } returns Unit

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                EditWorkoutScreen(
                    workoutId = 1,
                    navigateToAddExercise = {},
                    navigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.save))
            .performClick()

        assertThat(viewModel.uiState.value).isEqualTo(EditWorkoutUiState.EditWorkoutSaved)
    }
}
