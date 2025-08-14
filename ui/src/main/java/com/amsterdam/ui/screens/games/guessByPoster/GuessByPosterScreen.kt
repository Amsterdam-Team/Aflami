package com.amsterdam.ui.screens.games.guessByPoster

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.GuessPicture
import com.amsterdam.ui.components.guessGame.TimerComponent
import com.amsterdam.ui.components.selection.AnswerSelectionItem
import com.amsterdam.ui.components.selection.AnswerStatus
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
                    navigationManager.navigateUp()
                }

                is GuessMovieByPosterGameEffect.NavigateToGameResult -> {
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
    Box {
        LoginBackground()
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(bottom = 16.dp)
                    .navigationBarsPadding(),
        ) {
            val pagerState = rememberPagerState(pageCount = { state.questions.size })
            val scope = rememberCoroutineScope()
            val topBarTitle = stringResource(com.amsterdam.ui.R.string.guess_movie_game_title)

            LaunchedEffect(state.currentQuestionIndex) {
                scope.launch { pagerState.animateScrollToPage(state.currentQuestionIndex) }
            }

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

            Box(modifier = Modifier.weight(1f))

            ConfirmButton(
                title = stringResource(R.string.next),
                onClick = interactionListener::onMoveToNextQuestion,
                isEnabled = state.isNextEnabled,
                isLoading = state.isLoading,
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
        var blurRadius by remember { mutableStateOf(8.dp) }
        GuessPicture(
            blurRadius = blurRadius,
            points = 10,
            imageUrl = question.posterUrl,
            isHintVisible = isHintEnabled,
            onClick = {
                blurRadius = 5.dp
                onHintClick()
            },
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(question.movieNameChoices) { index, answer ->
                val status =
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
                    status = status,
                    onClick = {
                        if (isChoiceEnabled) return@AnswerSelectionItem
                        onSelectAnswer(index)
                    },
                )
            }
        }
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
                painter = painterResource(R.drawable.ic_cancel),
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
                },
        )
    }
}