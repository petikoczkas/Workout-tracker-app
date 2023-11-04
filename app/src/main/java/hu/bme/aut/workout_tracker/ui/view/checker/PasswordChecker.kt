package hu.bme.aut.workout_tracker.ui.view.checker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography

@Composable
fun PasswordChecker(
    password: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Password has to contain:",
            style = workoutTrackerTypography.passwordCheckerTitleTextStyle,
        )
        CheckerRow(text = "At least 8 characters ") {
            if (password.length >= 8) CheckIcon() else CancelIcon()
        }
        CheckerRow(text = "An upper case letter") {
            if (password.contains("[A-Z]".toRegex())) CheckIcon() else CancelIcon()
        }

        CheckerRow(text = "A lower case letter") {
            if (password.contains("[a-z]".toRegex())) CheckIcon() else CancelIcon()
        }
        CheckerRow(text = "A number") {
            if (password.contains("\\d".toRegex())) CheckIcon() else CancelIcon()
        }
    }
}

@Composable
private fun CheckerRow(
    text: String,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = workoutTrackerDimens.gapSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(
            text = text,
            style = workoutTrackerTypography.passwordCheckerDescriptionTextStyle,
            modifier = Modifier.padding(start = workoutTrackerDimens.gapMedium)
        )
    }
}

@Composable
private fun CheckIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_check_circle),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun CancelIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_cancel_circle),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error
    )
}