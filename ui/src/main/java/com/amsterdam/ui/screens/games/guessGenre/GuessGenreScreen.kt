package com.amsterdam.ui.screens.games.guessGenre

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Scaffold
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.GameTopBar
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.AdaptiveAnswersColumn
import com.amsterdam.ui.components.guessGame.GuessTitle
import com.amsterdam.ui.screens.letsPlay.component.NotEnoughPointsDialog
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.uiModel
import com.amsterdam.viewmodel.game.whichGenre.GameQuestionUiState
import com.amsterdam.viewmodel.game.whichGenre.GenreGameEffect
import com.amsterdam.viewmodel.game.whichGenre.GenreGameInteractionListener
import com.amsterdam.viewmodel.game.whichGenre.GenreGameUiState
import com.amsterdam.viewmodel.game.whichGenre.GuessGenreViewModel
import kotlinx.coroutines.launch

@Composable
fun GuessGenreScreen(
    viewModel: GuessGenreViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GenreGameEffect.CancelGame -> {
                    navigationManager.toLetsPlay(clearBackStack = true)
                }

                is GenreGameEffect.GameOver -> {
                    val resultScreenData = effect.resultScreenData
                    navigationManager.toResultScreen(
                        totalCollectedPoints = resultScreenData.totalCollectedPoints,
                        totalSpentSeconds = resultScreenData.totalSpentSeconds,
                        gameType = resultScreenData.gameType,
                        difficulty = resultScreenData.difficulty
                    )
                }
            }
        }
    }

    GameScreenContent(state.value, viewModel)
}

@Composable
private fun GameScreenContent(
    state: GenreGameUiState,
    interactionListener: GenreGameInteractionListener
) {
    val pagerState = rememberPagerState(pageCount = { state.questions.size })
    val scope = rememberCoroutineScope()

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
                    modifier = Modifier
                        .fillMaxSize().statusBarsPadding()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    item {
                        GameTopBar(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            title = stringResource(R.string.genre_game_title),
                            timerUiState = state.timerUiState,
                            onCancelGameClick = interactionListener::onCancelGameClick
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
                                onHintClick = interactionListener::onUseHint,
                                onSelectAnswer = interactionListener::onChooseAnswerClick,
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
    question: GameQuestionUiState,
    selectedAnswerIndex: Int?,
    isAnswerCorrect: Boolean?,
    isHintEnabled: Boolean,
    modifier: Modifier = Modifier,
    isChoiceEnabled: Boolean = true,
    onHintClick: () -> Unit = {},
    onSelectAnswer: (answerIndex: Int) -> Unit = { },
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GuessTitle(
            title = question.questionData,
            points = 10,
            isHintVisible = isHintEnabled,
            onClick = onHintClick,
        )
        AdaptiveAnswersColumn(
            question.answers.map { stringResource(it.uiModel.displayableName) },
            selectedAnswerIndex,
            isAnswerCorrect,
            isChoiceEnabled,
            onSelectAnswer
        )

    }
}