package hu.bme.aut.workout_tracker.ui.screen.workout.workout

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.ContentAlpha
import hu.bme.aut.workout_tracker.R
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutLoadedUiState.*
import hu.bme.aut.workout_tracker.ui.screen.workout.workout.WorkoutUiState.*
import hu.bme.aut.workout_tracker.ui.screen.workout.workoutcomplete.WorkoutCompleteScreen
import hu.bme.aut.workout_tracker.ui.theme.workoutTrackerDimens
import hu.bme.aut.workout_tracker.ui.view.button.PrimaryButton
import hu.bme.aut.workout_tracker.ui.view.button.SecondaryButton
import hu.bme.aut.workout_tracker.ui.view.table.TableRow
import hu.bme.aut.workout_tracker.ui.view.table.TextTableCell
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutScreen(
    workoutId: String,
    navigateToAddExercise: (String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val loadedUiState by viewModel.loadedUiState.collectAsState()
    val workoutExercises by viewModel.workoutExercises.observeAsState()
    val saveFailedEvent by viewModel.saveFailedEvent.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    when (uiState) {
        WorkoutInit -> {
            viewModel.getWorkout(workoutId)
        }

        is WorkoutLoaded -> {
            workoutExercises?.let {
                viewModel.getWorkoutExercises()
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    initialPageOffsetFraction = 0f,
                    pageCount = {
                        when (loadedUiState) {
                            is Loaded -> {
                                (loadedUiState as Loaded).pageCount
                            }

                            else -> 0
                        }
                    }
                )
                when (loadedUiState) {
                    AddExercise -> {
                        viewModel.switchOrAddCurrentExercise("addExercise")
                        coroutineScope.launch {
                            delay(200)
                            pagerState.animateScrollToPage(pagerState.pageCount - 2)
                        }
                    }

                    is Loaded -> {
                        HorizontalPager(state = pagerState) { page ->
                            if (viewModel.exercises.size > page) {
                                WorkoutScreenContent(
                                    pagerState = pagerState,
                                    page = page,
                                    uiState = uiState,
                                    viewModel = viewModel,
                                    navigateToAddExercise = { navigateToAddExercise(workoutId) },
                                    localFocusManager = LocalFocusManager.current
                                )
                            } else {
                                WorkoutCompleteScreen(
                                    navigateToAddExercise = {
                                        viewModel.changeLoadedUiState(AddExercise)
                                        navigateToAddExercise(workoutId)
                                    },
                                    navigateBack = {
                                        viewModel.endWorkoutOnClick()
                                        navigateBack()
                                    }
                                )
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(bottom = workoutTrackerDimens.gapNormal),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            repeat((loadedUiState as Loaded).pageCount) {
                                val color =
                                    if (pagerState.currentPage == it) Color.DarkGray else Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(10.dp)

                                )
                            }
                        }
                    }
                }
            }
            if (saveFailedEvent.isSaveFailed) {
                Toast.makeText(
                    LocalContext.current,
                    saveFailedEvent.exception?.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.handledSaveFailedEvent()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun WorkoutScreenContent(
    pagerState: PagerState,
    page: Int,
    uiState: WorkoutUiState,
    viewModel: WorkoutViewModel,
    localFocusManager: FocusManager,
    navigateToAddExercise: () -> Unit
) {
    val alpha = if ((uiState as WorkoutLoaded).isEnabledList[page]) 1f else ContentAlpha.disabled
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val requestFocus by remember {
        derivedStateOf {
            pagerState.currentPageOffsetFraction == 0f && page == pagerState.currentPage
        }
    }

    LaunchedEffect(key1 = requestFocus) {
        if (requestFocus) {
            delay(200)
            keyboardController?.show()
            focusRequester.requestFocus()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = workoutTrackerDimens.gapNormal,
                end = workoutTrackerDimens.gapNormal,
                bottom = 50.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = viewModel.getExerciseName(page = page),
                modifier = Modifier.alpha(alpha)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = workoutTrackerDimens.gapSmall),
                    ) {
                        TextTableCell(
                            text = stringResource(R.string.sets),
                            weight = 2f,
                            modifier = Modifier.alpha(alpha)
                        )
                        TextTableCell(
                            text = stringResource(R.string.weight_kg),
                            weight = 3f,
                            modifier = Modifier.alpha(alpha)
                        )
                        TextTableCell(text = "", weight = 1f, modifier = Modifier.alpha(alpha))
                        TextTableCell(
                            text = stringResource(R.string.reps),
                            weight = 2f,
                            modifier = Modifier.alpha(alpha)
                        )
                    }
                }
                items(uiState.weightList[page].size) { set ->
                    val imeAction = ImeAction.Next

                    val keyboardActions = KeyboardActions(
                        onNext = { localFocusManager.moveFocus(FocusDirection.Next) }
                    )

                    TableRow(
                        set = "${set + 1}.",
                        weight = uiState.weightList[page][set],
                        onWeightChange = {
                            viewModel.onWeightListChange(
                                weight = it,
                                pageNum = page,
                                setNum = set
                            )
                        },
                        reps = uiState.repsList[page][set],
                        onRepsChange = {
                            viewModel.onRepsListChange(
                                reps = it,
                                pageNum = page,
                                setNum = set
                            )
                        },
                        enabled = uiState.isEnabledList[page],
                        cleared = viewModel.switchOrAddCurrentExercise(page.toString()),
                        keyboardActions = keyboardActions,
                        imeAction = imeAction,
                        isLast = set + 1 == uiState.weightList[page].size,
                        keyboardActionsLast = KeyboardActions(onDone = { localFocusManager.clearFocus() }),
                        modifier = if (set == 0) Modifier.focusRequester(focusRequester) else Modifier
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { viewModel.removeButtonOnClick(page) },
                            enabled = uiState.isEnabledList[page] && uiState.weightList[page].size != 1
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_remove),
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = { viewModel.addButtonOnClick(page) },
                            enabled = uiState.isEnabledList[page]
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null
                            )
                        }
                    }
                    SecondaryButton(
                        onClick = navigateToAddExercise,
                        text = stringResource(R.string.switch_exercise),
                        enabled = uiState.isEnabledList[page]
                    )
                }
            }
        }
        PrimaryButton(
            onClick = {
                viewModel.saveButtonOnClick(page)
                if (page + 1 < pagerState.pageCount) {
                    if (pagerState.pageCount - 1 == page + 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page + 1)
                        }
                    } else if (uiState.isEnabledList[page + 1]) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page + 1)
                        }
                    }
                }
            },
            text = stringResource(R.string.save),
            enabled = uiState.isEnabledList[page] && viewModel.isSaveButtonEnabled(
                page
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = workoutTrackerDimens.gapMedium)
        )
    }
}
