package hu.bme.aut.workout_tracker.ui.screen.standings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StandingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: StandingViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter


    @Before
    fun setup() {
        workoutTrackerPresenter = mockk()
        viewModel = StandingViewModel(workoutTrackerPresenter)

        val exercise = Exercise(id = 1, category = "Chest", name = "Bench Press")
        val user = User(
            email = "test@test.com",
            firstName = "John",
            lastName = "Doe",
            photo = byteArrayOf()
        )

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns user
        coEvery { workoutTrackerPresenter.getCharts() } returns listOf(
            Chart(
                id = 0,
                userId = "test@test.com",
                exercise = exercise,
                type = ChartType.OneRepMax,
                data = mutableListOf(60.0, 80.0, 100.0)
            )
        )
        coEvery { workoutTrackerPresenter.getStandingsExercises() } returns listOf(exercise)
        coEvery { workoutTrackerPresenter.getUsers() } returns listOf(user)
    }

    @Test
    fun testLoadedStateDisplaysTitleAndDropdown() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                StandingsScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.standings))
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("WorkoutTrackerDropDownMenu").assertIsDisplayed()
    }

    @Test
    fun testLoadedStateWithNoDataDisplaysErrorMessage() {
        coEvery { workoutTrackerPresenter.getCharts() } returns emptyList()

        composeTestRule.setContent {
            WorkoutTrackerTheme {
                StandingsScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Select a Body Part").performClick()
        composeTestRule.onNodeWithText("Bench Press").performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.standings_error_message))
            .assertIsDisplayed()
    }

    @Test
    fun testLoadedStateDisplaysUserCards() {
        composeTestRule.setContent {
            WorkoutTrackerTheme {
                StandingsScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Select a Body Part").performClick()
        composeTestRule.onNodeWithText("Bench Press").performClick()

        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("100 kg").assertIsDisplayed()
    }
}
