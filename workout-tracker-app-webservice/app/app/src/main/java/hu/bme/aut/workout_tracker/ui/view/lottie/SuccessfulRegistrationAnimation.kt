package hu.bme.aut.workout_tracker.ui.view.lottie

import android.graphics.Color
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.ColorUtils
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens

@Composable
fun SuccessfulRegistrationAnimation() {
    val darkerPrimary =
        ColorUtils.blendARGB(MaterialTheme.colorScheme.primary.toArgb(), Color.BLACK, 0.5f)
    val dynamicProperties = rememberLottieDynamicProperties(
        //check
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialTheme.colorScheme.primary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 2",
                "Shape 1",
                "Stroke 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialTheme.colorScheme.primary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "New_Plane",
                "Shape Layer 3",
                "Shape 1",
                "Fill 1"
            )
        ),
        //plane
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                darkerPrimary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 1",
                "Shape 1",
                "Stroke 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                darkerPrimary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "New_Plane",
                "Shape Layer 4",
                "Shape 1",
                "Fill 1"
            )
        ),
        //balls
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialTheme.colorScheme.primary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 3",
                "Ellipse 1",
                "Stroke 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                darkerPrimary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 3",
                "Ellipse 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialTheme.colorScheme.primary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 4",
                "Ellipse 1",
                "Stroke 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                darkerPrimary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 4",
                "Ellipse 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialTheme.colorScheme.primary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 5",
                "Ellipse 1",
                "Stroke 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                darkerPrimary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "Shape Layer 5",
                "Ellipse 1",
                "Fill 1"
            )
        ),
    )

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.successful_registration)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = false

    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        dynamicProperties = dynamicProperties,
        modifier = Modifier
            .size(workoutTrackerDimens.lottieAnimationSize)
            .padding(
                top = workoutTrackerDimens.gapVeryLarge,
                bottom = workoutTrackerDimens.gapLarge
            )
            .testTag("SuccessfulRegistrationAnimation")
    )
}