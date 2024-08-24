package hu.bme.aut.workout_tracker.ui.screen.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter
    private lateinit var navigateToWorkout: (String) -> Unit
    private lateinit var navigateToSettings: () -> Unit

    @Before
    fun setup() {
        hiltRule.inject()
        workoutTrackerPresenter = mockk()
        viewModel = HomeViewModel(workoutTrackerPresenter)
        navigateToWorkout = mockk(relaxed = true)
        navigateToSettings = mockk(relaxed = true)

        val dummyUser = User()

        coEvery { workoutTrackerPresenter.getUserFavoriteWorkouts(any()) } returns emptyList()

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns dummyUser
        coEvery { workoutTrackerPresenter.getUserCharts(any()) } returns emptyList()
        coEvery { workoutTrackerPresenter.getStandingsExercises() } returns emptyList()
    }

    @Test
    fun testHomeScreenDisplaysGreetingAndSettingsIcon() {

        composeTestRule.setContent {
            HomeScreen(navigateToWorkout, navigateToSettings, viewModel)
        }

        composeTestRule.onNodeWithText("Hi !").assertExists()
        composeTestRule.onNodeWithContentDescription("Settings Icon").assertExists()
    }

    @Test
    fun testHomeScreenDisplaysFavoriteWorkouts() {
        val dummyWorkouts = listOf(
            Workout(
                id = 1,
                userId = "0",
                name = "Workout 1",
                isFavorite = true,
                exercises = mutableListOf()
            ),
            Workout(
                id = 2,
                userId = "0",
                name = "Workout 2",
                isFavorite = true,
                exercises = mutableListOf()
            )
        )

        coEvery { workoutTrackerPresenter.getUserFavoriteWorkouts(any()) } returns dummyWorkouts

        composeTestRule.setContent {
            HomeScreen(navigateToWorkout, navigateToSettings, viewModel)
        }

        composeTestRule.onNodeWithText("Workout 1").assertExists()
        composeTestRule.onNodeWithText("Workout 2").assertExists()
    }

    @Test
    fun testHomeScreenDisplaysNoFavoriteWorkoutsMessage() {
        composeTestRule.setContent {
            HomeScreen(navigateToWorkout, navigateToSettings, viewModel)
        }

        composeTestRule.onNodeWithText("You Have No Favorite Workouts").assertExists()
    }

    @Test
    fun testClickOnSettingsIconNavigatesToSettings() {
        composeTestRule.setContent {
            HomeScreen(navigateToWorkout, navigateToSettings, viewModel)
        }

        composeTestRule.onNodeWithContentDescription("Settings Icon").performClick()

        coEvery { navigateToSettings.invoke() }
    }
}
