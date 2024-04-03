package hu.bme.aut.workout_tracker.ui.screen.main

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.workout_tracker.ui.navigation.MainNavGraph
import hu.bme.aut.workout_tracker.ui.view.bottombar.BottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.P)
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