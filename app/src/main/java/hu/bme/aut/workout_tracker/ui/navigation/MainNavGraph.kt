package hu.bme.aut.workout_tracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsScreen
import hu.bme.aut.workout_tracker.ui.screen.home.HomeScreen
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsScreen
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingsScreen
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                onSettingsClick = {
                    navController.navigate(Content.Settings.route)
                }
            )
        }
        composable(route = BottomBarScreen.Workout.route) {
            YourWorkoutsScreen()
        }
        composable(route = BottomBarScreen.Charts.route) {
            ChartsScreen()
        }
        composable(route = BottomBarScreen.Standings.route) {
            StandingsScreen()
        }
        composable(route = BottomBarScreen.Standings.route) {
            StandingsScreen()
        }
        composable(route = Content.Settings.route) {
            SettingsScreen(
                onClick = {
                    navController.navigate(BottomBarScreen.Home.route)
                }
            )
        }
    }
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    data object Home :
        BottomBarScreen(route = "HOME", title = "HOME", icon = R.drawable.ic_home)

    data object Workout :
        BottomBarScreen(route = "WORKOUT", title = "WORKOUT", icon = R.drawable.ic_workout)

    data object Charts :
        BottomBarScreen(route = "CHARTS", title = "CHARTS", icon = R.drawable.ic_charts)

    data object Standings :
        BottomBarScreen(route = "STANDINGS", title = "STANDINGS", icon = R.drawable.ic_standings)
}

sealed class Content(val route: String) {
    data object Settings : Content(route = "SETTINGS")
}