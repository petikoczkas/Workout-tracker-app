package hu.bme.aut.workout_tracker.ui.screen.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerTypography
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.dialog.LoadingDialog
import hu.bme.aut.workout_tracker.ui.view.dialog.WorkoutTrackerAlertDialog
import hu.bme.aut.workout_tracker.ui.view.textfield.WorkoutTrackerTextField

@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    signOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateUserFailedEvent by viewModel.updateUserFailedEvent.collectAsState()
    val showSavingDialog by viewModel.savingState.collectAsState()

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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = workoutTrackerDimens.gapLarge)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = workoutTrackerTypography.titleTextStyle,
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
                    )
                    Box(
                        modifier = Modifier
                            .size(workoutTrackerDimens.settingsImageSize)
                            .padding(workoutTrackerDimens.gapNormal)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.currentUser.photo.isEmpty() && (uiState as SettingsLoaded).imageUri == Uri.EMPTY) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .size(workoutTrackerDimens.settingsImageContentSize)
                            )
                        } else {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(workoutTrackerDimens.settingsImageSize)
                            )
                        }
                    }
                    WorkoutTrackerTextField(
                        text = (uiState as SettingsLoaded).name,
                        onTextChange = viewModel::onNameChange,
                        placeholder = stringResource(R.string.name),
                        modifier = Modifier.padding(vertical = workoutTrackerDimens.gapVeryLarge)
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
                if (showSavingDialog) {
                    LoadingDialog()
                }
            }
        }

        SettingsSaved -> {
            navigateBack()
        }
    }
}