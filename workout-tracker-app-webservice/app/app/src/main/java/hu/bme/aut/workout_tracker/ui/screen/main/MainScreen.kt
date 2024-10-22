package hu.bme.aut.workout_tracker.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.workout_tracker.ui.navigation.MainNavGraph
import hu.bme.aut.workout_tracker.ui.view.bottombar.BottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainNavController: NavHostController,
    navController: NavHostController = rememberNavController()
) {

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        MainNavGraph(
            mainNavController = mainNavController,
            navController = navController
        )
    }
}