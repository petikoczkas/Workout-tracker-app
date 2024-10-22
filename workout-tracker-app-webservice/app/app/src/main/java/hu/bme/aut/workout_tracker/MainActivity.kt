package hu.bme.aut.workout_tracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.workout_tracker.service.StepCounterService
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
    private var serverStatusTimer: Timer? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var hasPermission by remember {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            mutableStateOf(
                                ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACTIVITY_RECOGNITION
                                ) == PackageManager.PERMISSION_GRANTED
                            )
                        } else mutableStateOf(true)
                    }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            hasPermission = isGranted
                        }
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        SideEffect {
                            launcher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                        }
                    }
                    if (hasPermission) {
                        this.startService(Intent(this, StepCounterService::class.java))
                    }

                    val connection by connectivityState()
                    val isConnected = connection === ConnectionState.Available
                    NoInternetDialog(isConnected = isConnected)

                    if (isConnected) startCheckServerStatusTask() else stopCheckServerStatusTask()

                    val serverStatus by viewModel.isServerAvailable.collectAsState()
                    viewModel.updateServerAvailability()
                    NoServerConnectionDialog(isAvailable = serverStatus)
                    RootNavigationGraph(navController = rememberNavController())
                }
            }
        }
    }

    private fun startCheckServerStatusTask() {
        if (serverStatusTimer != null) {
            serverStatusTimer = Timer()
            serverStatusTimer?.schedule(object : TimerTask() {
                override fun run() {
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.updateServerAvailability()
                    }
                }
            }, 0, 10 * 1000) // Run every 10 seconds
        }
    }

    private fun stopCheckServerStatusTask() {
        serverStatusTimer?.cancel()
        serverStatusTimer = null
    }
}