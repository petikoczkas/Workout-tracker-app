package hu.bme.aut.workout_tracker.ui.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun EndWorkoutDialog(
    navigateBack: () -> Unit,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.end_workout)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        isPlaying = true,
        speed = 0.5f,
        restartOnPlay = false

    )
    if (progress != 1.0f) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(workoutTrackerDimens.gapNormal),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(workoutTrackerDimens.lottieAnimationSize)
                )
            }
        }
    } else {
        navigateBack()
    }
}