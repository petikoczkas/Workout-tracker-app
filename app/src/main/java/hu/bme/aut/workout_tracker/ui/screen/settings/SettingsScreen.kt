package hu.bme.aut.workout_tracker.ui.screen.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsInit
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsLoaded
import hu.bme.aut.workout_tracker.ui.screen.settings.SettingsUiState.SettingsSaved
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.dialog.WorkoutTrackerAlertDialog

@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    signOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateUserFailedEvent by viewModel.updateUserFailedEvent.collectAsState()

    when (uiState) {
        SettingsInit -> {
            viewModel.getCurrentUser()
        }

        is SettingsLoaded -> {
            val painter = rememberAsyncImagePainter(
                if ((uiState as SettingsLoaded).imageUri == Uri.EMPTY) {
                    viewModel.currentUser.photo
                } else (uiState as SettingsLoaded).imageUri
            )
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    viewModel.onImageChange(it)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapNormal),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.signOut()
                                signOut()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logout),
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.settings),
                    )
                    Card(
                        modifier = Modifier
                            .size(workoutTrackerDimens.settingsImageSize)
                            .padding(workoutTrackerDimens.gapNormal)
                            .clip(CircleShape)
                            .clickable { launcher.launch("image/*") }
                    ) {
                        if (viewModel.currentUser.photo.isEmpty() && (uiState as SettingsLoaded).imageUri == Uri.EMPTY) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(workoutTrackerDimens.settingsImageContentSize)
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(workoutTrackerDimens.settingsImageSize)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    TextField(
                        value = (uiState as SettingsLoaded).name,
                        onValueChange = viewModel::onNameChange,
                        label = { Text(text = stringResource(R.string.name)) },
                        modifier = Modifier
                            .padding(
                                vertical = workoutTrackerDimens.gapLarge,
                            )
                    )
                }
                PrimaryButton(
                    onClick = { viewModel.saveButtonOnClick() },
                    text = stringResource(R.string.save),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = workoutTrackerDimens.gapNormal)
                )
                if (updateUserFailedEvent.isUpdateUserFailed) {
                    WorkoutTrackerAlertDialog(
                        title = stringResource(R.string.update_profile_failed),
                        description = updateUserFailedEvent.exception?.message.toString(),
                        onDismiss = { viewModel.handledUpdateUserFailedEvent() }
                    )
                }
            }
        }

        SettingsSaved -> {
            navigateBack()
        }
    }
}