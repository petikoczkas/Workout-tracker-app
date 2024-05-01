package hu.bme.aut.workout_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.workout_tracker.ui.navigation.RootNavigationGraph
import hu.bme.aut.workout_tracker.ui.theme.WorkoutTrackerTheme
import hu.bme.aut.workout_tracker.ui.view.dialog.NoInternetDialog
import hu.bme.aut.workout_tracker.ui.view.dialog.NoServerConnectionDialog
import hu.bme.aut.workout_tracker.ui.view.network.ConnectionState
import hu.bme.aut.workout_tracker.ui.view.network.connectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCheckServerStatusTask()
        setContent {
            WorkoutTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val connection by connectivityState()
                    val isConnected = connection === ConnectionState.Available
                    NoInternetDialog(isConnected = isConnected)

                    val serverStatus by viewModel.isServerAvailable.collectAsState()
                    viewModel.updateServerAvailability()
                    NoServerConnectionDialog(isAvailable = serverStatus)
                    RootNavigationGraph(navController = rememberNavController())
                }
            }
        }
    }

    private fun startCheckServerStatusTask() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.updateServerAvailability()
                }
            }
        }, 0, 10 * 1000) // Run every 10 seconds
    }
}