package com.amsterdam.ui.screens.games.character

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.GameTopBar
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.GuessPicture
import com.amsterdam.ui.components.selection.AnswerSelectionItem
import com.amsterdam.ui.components.selection.AnswerStatus
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterGameEffect
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterGameViewModel
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun GuessCharacterScreen(viewModel: GuessCharacterGameViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GuessCharacterGameEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is GuessCharacterGameEffect.NavigateToGame -> {
                    navController.navigate(Route.Tab.LetsPlay)
                }

                is GuessCharacterGameEffect.NavigateToGameResult -> {
                    navController.navigate(
                        Route.ResultScreen(
                            effect.totalCollectedPoints,
                            effect.totalSpentSeconds
                        )
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
    Box {
        LoginBackground()
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            GameTopBar(
                title = stringResource(R.string.guess_character_game_title),
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
                CharacterGameQuestion(
                    questionImageModel = question.characterImageUrl,
                    answers = question.characterChoices,
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
fun CharacterGameQuestion(
    questionImageModel: String,
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

        val animatedBlur by animateDpAsState(
            targetValue = if (isHintEnabled) 8.dp else 5.dp,
        )

        GuessPicture(
            blurRadius = animatedBlur,
            points = 10,
            imageUrl = questionImageModel,
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
                        if (isAnswerCorrect != null && isChoicesEnabled) return@AnswerSelectionItem
                        onSelectAnswer(index)
                    },
                )
            }
        }
    }
}