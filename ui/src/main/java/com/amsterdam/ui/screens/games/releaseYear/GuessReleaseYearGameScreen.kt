package com.amsterdam.ui.screens.games.releaseYear

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
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Scaffold
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.screens.games.component.GameTopBar
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.screens.games.component.AdaptiveAnswersColumn
import com.amsterdam.ui.screens.games.component.GuessTitle
import com.amsterdam.ui.screens.games.component.NotEnoughPointsDialog
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearGameEffect
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearGameViewModel
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearInteractionListener
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun GuessReleaseYearScreen(viewModel: GuessReleaseYearGameViewModel = hiltViewModel()) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GuessReleaseYearGameEffect.NavigateBack -> {
                    navigationManager.toLetsPlay(clearBackStack = true)
                }

                is GuessReleaseYearGameEffect.NavigateToGameResult -> {
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
    GameContent(state.value, viewModel)
}

@Composable
private fun GameContent(
    state: GuessReleaseYearUiState,
    interactionListener: GuessReleaseYearInteractionListener,
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
                    Modifier
                        .fillMaxSize().statusBarsPadding()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    item {
                        GameTopBar(
                            title = stringResource(R.string.release_game_title),
                            timerUiState = state.timerUiState,
                            onCancelGameClick = interactionListener::onClickClose,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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
                            val question = state.questions[page]
                            ReleaseYearGameQuestion(
                                question = question.movieName,
                                answers = question.releaseYearAnswer,
                                selectedAnswerIndex = state.selectedAnswerIndex,
                                isAnswerCorrect = state.isAnswerCorrect,
                                isHintEnabled = state.isHintEnabled,
                                isChoicesEnabled = state.isNextEnabled,
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
fun ReleaseYearGameQuestion(
    question: String,
    answers: List<String>,
    selectedAnswerIndex: Int?,
    isAnswerCorrect: Boolean?,
    isHintEnabled: Boolean,
    modifier: Modifier = Modifier,
    isChoicesEnabled: Boolean = true,
    onHintClick: () -> Unit = {},
    onSelectAnswer: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GuessTitle(
            title = question,
            points = 10,
            isHintVisible = isHintEnabled,
            onClick = onHintClick,
        )

        AdaptiveAnswersColumn(
            answers,
            selectedAnswerIndex,
            isAnswerCorrect,
            isChoicesEnabled,
            onSelectAnswer
        )
    }
}