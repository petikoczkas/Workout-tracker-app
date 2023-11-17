package hu.bme.aut.workout_tracker.ui.view.textfield

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import hu.bme.aut.workout_tracker.R

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPasswordAgain: Boolean = false,
    firstPassword: String? = null
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    WorkoutTrackerTextField(
        text = password,
        onTextChange = onPasswordChange,
        placeholder = if (isPasswordAgain) stringResource(R.string.password_again) else stringResource(
            R.string.password
        ),
        isError = if (isPasswordAgain && password.isNotEmpty()) firstPassword != password else false,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = null
            )
        },
        trailingIcon = {
            val image: Painter =
                if (passwordVisible) {
                    painterResource(id = R.drawable.ic_visibility)
                } else {
                    painterResource(id = R.drawable.ic_visibility_off)
                }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, contentDescription = null)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyBoardType = KeyboardType.Password,
        modifier = modifier
    )
}