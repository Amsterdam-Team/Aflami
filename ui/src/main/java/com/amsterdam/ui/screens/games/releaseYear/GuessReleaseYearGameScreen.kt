package com.amsterdam.ui.screens.games.releaseYear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.GameTopBar
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.GuessTitle
import com.amsterdam.ui.components.selection.AnswerSelectionItem
import com.amsterdam.ui.components.selection.AnswerStatus
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
                    navigationManager.navigateUp()
                }
                is GuessReleaseYearGameEffect.NavigateToGameResult -> {
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
    GameContent(state.value,viewModel)
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
    Box {
        LoginBackground()
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
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
            Spacer(modifier = Modifier.weight(1f))
            ConfirmButton(
                title = stringResource(com.amsterdam.designsystem.R.string.next),
                onClick = interactionListener::onMoveToNextQuestion,
                isEnabled = state.isNextEnabled,
                isLoading = false,
                isNegative = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
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


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(answers) { index, answer ->
                val state =
                    if (selectedAnswerIndex == index) {
                        when (isAnswerCorrect) {
                            true -> AnswerStatus.Correct
                            false -> AnswerStatus.Wrong
                            null -> AnswerStatus.Unselected
                        }
                    } else {
                        AnswerStatus.Unselected
                    }

                AnswerSelectionItem(
                    text = answer,
                    status = state,
                    onClick = {
                        if (isChoicesEnabled) return@AnswerSelectionItem
                        onSelectAnswer(index)
                    },
                )
            }
        }
    }
}