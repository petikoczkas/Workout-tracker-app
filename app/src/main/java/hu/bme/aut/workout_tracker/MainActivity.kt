package hu.bme.aut.workout_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.workout_tracker.ui.navigation.RootNavigationGraph
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import hu.bme.aut.workout_tracker.ui.view.dialog.NoInternetDialog
import hu.bme.aut.workout_tracker.ui.view.network.ConnectionState
import hu.bme.aut.workout_tracker.ui.view.network.connectivityState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val connection by connectivityState()
                    val isConnected = connection === ConnectionState.Available
                    NoInternetDialog(isConnected = isConnected)
                    RootNavigationGraph(navController = rememberNavController())
                }
            }
        }
    }
}