package com.amsterdam.ui.screens.guessGenre

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amsterdam.ui.R as R2
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.GuessTitle
import com.amsterdam.ui.components.selection.AnswerSelectionItem
import com.amsterdam.ui.components.selection.AnswerStatus
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.game.GameTopBar
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
    val navController = LocalNavController.current
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GenreGameEffect.CancelGame -> {
                    navController.popBackStack()
                }

                is GenreGameEffect.GameOver -> {
                    navController.navigate(Route.ResultScreen(
                        totalCollectedPoints = effect.totalEarnedPoints,
                        totalSpentSeconds = effect.answersTotalTime
                    ))
                }

                GenreGameEffect.ShowNotEnoughPointsSnackBar -> {
                    SnackBarManager.showError(context.getString(R2.string.oops_there_are_not_enough_points))
                }
            }
        }
    }

    GameScreenContent(state.value, viewModel)
}

@Composable
private fun GameScreenContent(
    state: GenreGameUiState,
    listener: GenreGameInteractionListener
) {
    Box {
        LoginBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = 16.dp)
                .navigationBarsPadding()
        ) {
            val pagerState = rememberPagerState(pageCount = { state.questions.size })
            val scope = rememberCoroutineScope()

            LaunchedEffect(state.currentQuestionIndex) {
                scope.launch { pagerState.animateScrollToPage(state.currentQuestionIndex) }
            }
            GameTopBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                title = stringResource(com.amsterdam.ui.R.string.genre_game_title),
                timerUiState = state.timerUiState,
                onCancelGameClick = listener::onCancelGameClick
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
                GameQuestion(
                    question = state.questions[page],
                    isHintEnabled = state.isHintEnabled,
                    selectedAnswerIndex = state.selectedAnswerIndex,
                    isAnswerCorrect = state.isAnswerCorrect,
                    onHintClick = listener::onUseHint,
                    onSelectAnswer = { answerIndex, answer ->
                        listener
                            .onChooseAnswerClick(
                                answer,
                                answerIndex,
                            )
                    },
                )
            }

            Box(modifier = Modifier.weight(1f))

            ConfirmButton(
                title = stringResource(com.amsterdam.designsystem.R.string.next),
                onClick = listener::onMoveToNextQuestion,
                isEnabled = state.isNextEnabled,
                isLoading = false,
                isNegative = false,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            )
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
    onHintClick: () -> Unit = {},
    onSelectAnswer: (answerIndex: Int, answer: MovieGenre) -> Unit = { _, _ -> },
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(question.answers) { index, answer ->
                val state = if (selectedAnswerIndex == index) {
                    when (isAnswerCorrect) {
                        true -> AnswerStatus.Correct
                        false -> AnswerStatus.Wrong
                        null -> AnswerStatus.Unselected
                    }
                } else {
                    AnswerStatus.Unselected
                }

                AnswerSelectionItem(
                    text = stringResource(answer.uiModel.displayableName),
                    status = state,
                    onClick = {
                        if (isAnswerCorrect != null) return@AnswerSelectionItem
                        onSelectAnswer(index, answer)
                    }
                )
            }
        }
    }
}