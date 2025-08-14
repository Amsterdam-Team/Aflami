package com.amsterdam.ui.screens.games.character

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
import com.amsterdam.ui.components.GameTopBar
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.AdaptiveAnswersColumn
import com.amsterdam.ui.components.guessGame.GuessPicture
import com.amsterdam.ui.screens.games.component.NotEnoughPointsDialog
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterGameEffect
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterGameViewModel
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun GuessCharacterScreen(viewModel: GuessCharacterGameViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GuessCharacterGameEffect.NavigateBack -> {
                    navigationManager.toLetsPlay(clearBackStack = true)
                }

                is GuessCharacterGameEffect.NavigateToGameResult -> {
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

    GameContent(state.value, viewModel)
}

@Composable
private fun GameContent(
    state: GuessCharacterUiState,
    interactionListener: GuessCharacterGameViewModel,
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
                title = stringResource(com.amsterdam.designsystem.R.string.next),
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
                    modifier = Modifier
                        .fillMaxSize().statusBarsPadding()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Column {
                            GameTopBar(
                                title = stringResource(R.string.guess_character_game_title),
                                timerUiState = state.timerUiState,
                                onCancelGameClick = interactionListener::onCloseButtonClicked,
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
                    }

                    item {
                        HorizontalPager(
                            state = pagerState,
                            userScrollEnabled = false,
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            pageSpacing = 12.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                        ) { page ->
                            val question = state.questions.getOrNull(page)
                            if (question != null) {
                                CharacterGameQuestion(
                                    questionImageModel = question.characterImageUrl,
                                    answers = question.characterChoices,
                                    blurRadius = question.blurRadius,
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
}

@Composable
fun CharacterGameQuestion(
    questionImageModel: String,
    answers: List<String>,
    blurRadius: Int,
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

        GuessPicture(
            blurRadius = blurRadius.dp,
            points = 10,
            imageUrl = questionImageModel,
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