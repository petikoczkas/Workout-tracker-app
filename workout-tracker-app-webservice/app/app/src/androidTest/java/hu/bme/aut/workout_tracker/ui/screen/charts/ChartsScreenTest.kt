package hu.bme.aut.workout_tracker.ui.screen.charts

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.ChartType
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.ui.WorkoutTrackerPresenter
import hu.bme.aut.workout_tracker.ui.view.chart.WorkoutTrackerChart
import hu.bme.aut.workout_tracker.ui.view.circularprogressindicator.WorkoutTrackerProgressIndicator
import hu.bme.aut.workout_tracker.ui.view.dialog.ChartsInformationDialog
import hu.bme.aut.workout_tracker.ui.view.dropdownmenu.WorkoutTrackerNestedDropDownMenu
import hu.bme.aut.workout_tracker.ui.view.toggle.TriStateToggle
import hu.bme.aut.workout_tracker.utils.Constants.chartsList
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChartsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: ChartsViewModel
    private lateinit var workoutTrackerPresenter: WorkoutTrackerPresenter


    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val dummyExercise = Exercise()
    private val dummyChart = Chart(id = 0, userId = "0", exercise = dummyExercise, type = ChartType.Volume, data = mutableListOf())

    @Before
    fun setup() {
        hiltRule.inject()
        workoutTrackerPresenter = mockk()
        viewModel = ChartsViewModel(workoutTrackerPresenter)

        val dummyUser = User()
        val dummyCharts = listOf(
            Chart(
                id = 0,
                userId = "0",
                exercise = Exercise(),
                type = ChartType.Volume,
                data = mutableListOf()
            ),
            Chart(
                id = 1,
                userId = "0",
                exercise = Exercise(),
                type = ChartType.AverageOneRepMax,
                data = mutableListOf()
            )
        )
        val dummyExercises = listOf(Exercise())

        coEvery { workoutTrackerPresenter.getCurrentUser() } returns dummyUser
        coEvery { workoutTrackerPresenter.getUserCharts(any()) } returns dummyCharts
        coEvery { workoutTrackerPresenter.getExercises() } returns dummyExercises
    }

    @Test
    fun testChartsScreenDisplaysLoadedState() {
        composeTestRule.setContent {
            ChartsScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(context.getString(R.string.charts))
            .assertExists()
        /*composeTestRule.onNodeWithTag("WorkoutTrackerNestedDropDownMenu")
            .assertExists()
        composeTestRule.onNodeWithTag("TriStateToggle")
            .assertExists()
        composeTestRule.onNodeWithTag("WorkoutTrackerChart")
            .assertExists()*/
    }

    /*@Test
    fun testChartsScreenDisplaysErrorState() {
        viewModel.uiState.value = ChartsUiState.ChartsLoaded(
            selectedExercise = dummyExercise,
            selectedChart = dummyChart,
            showDialog = false
        )
        viewModel.exercises = MutableLiveData(listOf(dummyExercise))

        composeTestRule.setContent {
            ChartsScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(context.getString(R.string.charts))
            .assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.chart_error_message))
            .assertExists()
    }*/
}
