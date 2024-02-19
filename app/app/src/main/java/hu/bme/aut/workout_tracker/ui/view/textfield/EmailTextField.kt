package hu.bme.aut.workout_tracker.ui.view.textfield

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.utils.isValidEmail

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    WorkoutTrackerTextField(
        text = email,
        onTextChange = onEmailChange,
        placeholder = stringResource(R.string.email),
        isError = if (email.isNotEmpty()) !email.isValidEmail() else false,
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = null)
        },
        keyBoardType = KeyboardType.Email,
        modifier = modifier
    )
}