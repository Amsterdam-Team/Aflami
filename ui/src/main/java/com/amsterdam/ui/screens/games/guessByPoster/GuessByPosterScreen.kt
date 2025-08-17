package com.amsterdam.ui.screens.games.guessByPoster

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Scaffold
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.screens.games.component.AdaptiveAnswersColumn
import com.amsterdam.ui.screens.games.component.GuessPicture
import com.amsterdam.ui.screens.games.component.TimerComponent
import com.amsterdam.ui.screens.games.component.NotEnoughPointsDialog
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.viewmodel.guessMovieByPosterGame.GuessMovieByPosterGameEffect
import com.amsterdam.viewmodel.guessMovieByPosterGame.GuessMovieByPosterGameViewModel
import com.amsterdam.viewmodel.guessMovieByPosterGame.GuessMovieByPosterInteractionListener
import com.amsterdam.viewmodel.guessMovieByPosterGame.GuessMovieByPosterUiState
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun GuessByPosterGameScreen(
    modifier: Modifier = Modifier,
    viewModel: GuessMovieByPosterGameViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GuessMovieByPosterGameEffect.NavigateBack -> {
                    navigationManager.toLetsPlay(clearBackStack = true)
                }

                is GuessMovieByPosterGameEffect.NavigateToGameResult -> {
                    val resultScreenData = effect.resultScreenData
                    navigationManager.toResultScreen(
                        gameType = resultScreenData.gameType,
                        difficulty = resultScreenData.difficulty,
                        gameSessionId = resultScreenData.gameSessionId
                    )
                }
            }
        }
    }

    GuessByPosterContent(
        state = state,
        interactionListener = viewModel,
        modifier = modifier,
    )
}

@Composable
private fun GuessByPosterContent(
    state: GuessMovieByPosterUiState,
    interactionListener: GuessMovieByPosterInteractionListener,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { state.questions.size })
    val scope = rememberCoroutineScope()
    val topBarTitle = stringResource(R.string.guess_by_poster)

    LaunchedEffect(state.currentQuestionIndex) {
        scope.launch { pagerState.animateScrollToPage(state.currentQuestionIndex) }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ConfirmButton(
                    title = stringResource(R.string.next),
                    onClick = interactionListener::onMoveToNextQuestion,
                    isEnabled = state.isNextEnabled,
                    isLoading = false,
                    isNegative = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    ) { innerPadding ->

        Box {
            LoginBackground()
            AnimatedVisibility(state.isNotEnoughPointsDialogVisible) {
                NotEnoughPointsDialog(
                    onConfirm = interactionListener::dismissNotEnoughPointsDialog,
                    onDismiss = interactionListener::dismissNotEnoughPointsDialog,
                )
            }
            AnimatedVisibility(
                state.isLoading,
                enter = fadeIn(tween(600)),
                exit = fadeOut(tween(100)),
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingContainer()
                }
            }
            AnimatedVisibility(
                state.questions.isNotEmpty(),
                enter = fadeIn(tween(600)),
                exit = fadeOut(tween(100)),
            ) {
                LazyColumn(
                    modifier =
                        modifier
                            .fillMaxSize().statusBarsPadding()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    item {
                        GameTopBar(
                            title = topBarTitle,
                            timerUiState = state.timerUiState,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            onCancelGameClick = interactionListener::onCloseButtonClicked
                        )

                        Row(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            PageIndicator(
                                pageCount = pagerState.pageCount,
                                currentPage = pagerState.currentPage,
                            )
                        }
                    }
                    item {
                        HorizontalPager(
                            state = pagerState,
                            userScrollEnabled = false,
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            pageSpacing = 12.dp,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                        ) { page ->
                            GameQuestion(
                                question = state.questions[page],
                                isHintEnabled = state.isHintEnabled,
                                selectedAnswerIndex = state.selectedAnswerIndex,
                                isAnswerCorrect = state.isAnswerCorrect,
                                isChoiceEnabled = state.isNextEnabled,
                                onHintClick = interactionListener::onHintClicked,
                                onSelectAnswer = interactionListener::onSelectAnswer,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameQuestion(
    question: GuessMovieByPosterUiState.QuestionUiState,
    selectedAnswerIndex: Int?,
    isAnswerCorrect: Boolean?,
    isHintEnabled: Boolean,
    modifier: Modifier = Modifier,
    isChoiceEnabled: Boolean = true,
    onHintClick: () -> Unit = {},
    onSelectAnswer: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GuessPicture(
            blurRadius = question.blurRadius.dp,
            points = 10,
            imageUrl = question.posterUrl,
            isHintVisible = isHintEnabled,
            onClick = {
                onHintClick()
            },
        )
        AdaptiveAnswersColumn(
            question.movieNameChoices,
            selectedAnswerIndex,
            isAnswerCorrect,
            isChoiceEnabled,
            onSelectAnswer
        )
    }
}

@Composable
internal fun GameTopBar(
    title: String,
    timerUiState: TimerUiState,
    modifier: Modifier = Modifier,
    onCancelGameClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        title = {
            Text(
                text = title,
                color = AppTheme.color.title,
                style = AppTheme.textStyle.title.large,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingIcon = {
            IconButton(
                painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                tint = AppTheme.color.title,
                contentDescription = stringResource(R.string.back_to_menue),
                onClick = onCancelGameClick,
            )
        },
        trailingIcon = {
            TimerComponent(timerUiState)
        },
    )
}

@ThemeAndLocalePreviews
@Composable
private fun GuessByPosterScreenPreview() {
    AflamiTheme {
        GuessByPosterContent(
            state =
                GuessMovieByPosterUiState(
                    questions =
                        listOf(
                            GuessMovieByPosterUiState.QuestionUiState(
                                posterUrl = "A",
                                movieNameChoices =
                                    listOf(
                                        "A",
                                        "B",
                                        "C",
                                        "D",
                                    ),
                            ),
                        ),
                    isHintEnabled = true
                ),
            interactionListener =
                object : GuessMovieByPosterInteractionListener {
                    override fun onCloseButtonClicked() {}
                    override fun onHintClicked() {}
                    override fun onSelectAnswer(selectedAnswerIndex: Int) {}
                    override fun onMoveToNextQuestion() {}
                    override fun dismissNotEnoughPointsDialog() {}
                },
        )
    }
}