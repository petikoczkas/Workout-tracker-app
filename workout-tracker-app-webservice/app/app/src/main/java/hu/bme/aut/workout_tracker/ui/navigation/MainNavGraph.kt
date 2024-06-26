package hu.bme.aut.workout_tracker.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.charts.ChartsScreen
import hu.bme.aut.workout_tracker.ui.screen.home.HomeScreen
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsScreen
import hu.bme.aut.workout_tracker.ui.screen.standings.StandingsScreen
import hu.bme.aut.workout_tracker.ui.screen.workout.addexercise.AddExerciseScreen
import hu.bme.aut.workout_tracker.ui.screen.workout.editworkout.EditWorkoutScreen
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutScreen
import hu.bme.aut.workout_tracker.ui.screen.workout.yourworkouts.YourWorkoutsScreen
import hu.bme.aut.workout_tracker.ui.view.dialog.EndWorkoutDialog

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainNavGraph(
    mainNavController: NavHostController,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomBarScreen.Home.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                navigateToWorkout = {
                    navController.navigate("${Content.Workout.route}/$it")
                },
                navigateToSettings = {
                    navController.navigate(Content.Settings.route)
                }
            )
        }
        composable(route = BottomBarScreen.YourWorkouts.route) {
            YourWorkoutsScreen(
                navigateToWorkout = {
                    navController.navigate("${Content.Workout.route}/$it")
                },
                navigateToEditWorkout = {
                    navController.navigate("${Content.EditWorkout.route}/$it")
                },
                navigateToCreateWorkout = {
                    navController.navigate("${Content.EditWorkout.route}/${-1}")
                }
            )
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
                navigateBack = {
                    navController.navigate(BottomBarScreen.Home.route)
                },
                signOut = {
                    mainNavController.navigate(AuthScreen.SignIn.route) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = "${Content.Workout.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            it.arguments?.getInt("id")?.let { id ->
                WorkoutScreen(
                    workoutId = id,
                    navigateBack = {
                        navController.popBackStack()
                        navController.navigate(Dialog.EndWorkoutDialog.route)
                    },
                    navigateToAddExercise = { i ->
                        navController.navigate("${Content.AddExercise.route}/$i")
                    }
                )
            }
        }
        dialog(route = Dialog.EndWorkoutDialog.route) {
            EndWorkoutDialog(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "${Content.EditWorkout.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            it.arguments?.getInt("id")?.let { id ->
                EditWorkoutScreen(
                    workoutId = id,
                    navigateToAddExercise = { it1 ->
                        navController.navigate("${Content.AddExercise.route}/$it1")
                    },
                    navigateBack = {
                        navController.navigate(BottomBarScreen.YourWorkouts.route)
                    }
                )
            }
        }
        composable(
            route = "${Content.AddExercise.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            it.arguments?.getInt("id")?.let { id ->
                AddExerciseScreen(
                    workoutId = id,
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    data object Home :
        BottomBarScreen(route = "HOME", title = "Home", icon = R.drawable.ic_home)

    data object YourWorkouts :
        BottomBarScreen(route = "YOUR_WORKOUTS", title = "Workout", icon = R.drawable.ic_workout)

    data object Charts :
        BottomBarScreen(route = "CHARTS", title = "Charts", icon = R.drawable.ic_charts)

    data object Standings :
        BottomBarScreen(route = "STANDINGS", title = "Standings", icon = R.drawable.ic_standings)
}

sealed class Content(val route: String) {
    data object Settings : Content(route = "SETTINGS")
    data object Workout : Content(route = "WORKOUT")
    data object EditWorkout : Content(route = "EDIT_WORKOUT")
    data object AddExercise : Content(route = "ADD_EXERCISE")
}

sealed class Dialog(val route: String) {
    data object EndWorkoutDialog : Dialog(route = "END_WORKOUT_DIALOG")
}