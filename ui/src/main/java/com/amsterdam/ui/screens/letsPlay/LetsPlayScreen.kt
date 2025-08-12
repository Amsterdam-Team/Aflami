package com.amsterdam.ui.screens.letsPlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.GameCard
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.letsPlay.component.DifficultyLevelDialog
import com.amsterdam.ui.screens.letsPlay.component.PlayScreenAppBar
import com.amsterdam.viewmodel.letsPlay.LetsPlayEffect
import com.amsterdam.viewmodel.letsPlay.LetsPlayInteractionListener
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayViewModel

@Composable
fun LetsPlayScreen(viewModel: LetsPlayViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LetsPlayEffect.NavigateToGameScreen -> {
                    navController.navigate(Route.Game)
                }
            }
        }
    }
    LetsPlayScreenContent(state = state.value, interactionListener = viewModel)
}

@Composable
private fun LetsPlayScreenContent(
    state: LetsPlayUiState,
    interactionListener: LetsPlayInteractionListener,
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.surface)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            item {
                PlayScreenAppBar(totalScore = state.totalUserPoint)
            }

            items(state.games) {
                val gameCardData = it.gameTypeUiState.getGameTypeData()
                GameCard(
                    title = stringResource(gameCardData.title),
                    description = stringResource(gameCardData.description),
                    containerColor = gameCardData.containerColor,
                    borderColors = gameCardData.borderColors,
                    onCardClick = { interactionListener.onClickGameCard(it.gameTypeUiState) },
                    gameCardImageContentType = gameCardData.gameCardImageContentType,
                    modifier = Modifier.padding(top = 12.dp),
                    isPlayable = state.totalUserPoint <= it.requiredPoints,
                    unlockPrice = "${it.requiredPoints}"
                )
            }

        }

        AnimatedVisibility(
            visible = state.selectedGameTypeUiState != null,
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(1f),
            enter = fadeIn(animationSpec = tween(600)),
            exit = fadeOut(animationSpec = tween(0))
        ) {
            DifficultyLevelDialog(
                difficulties = state.difficulties,
                selectedDifficultyLevel = state.selectedDifficultyLevel,
                isStartGameButtonEnable = state.isStartGameButtonEnable,
                onLevelSelected = interactionListener::onSelectDifficultyLevel,
                onCloseDialogClicked = interactionListener::onClickCloseDifficultyLevelDialog,
                onClickStartGame = interactionListener::onClickStartGame
            )
        }

    }
}

@Preview
@Composable
private fun LetsPlayScreenPreview() {
    LetsPlayScreenContent(
        state = LetsPlayUiState(),
        interactionListener = object : LetsPlayInteractionListener {
            override fun onSelectDifficultyLevel(difficultyLevel: GameDifficultyUiState) {}
            override fun onClickCloseDifficultyLevelDialog() {}
            override fun onClickGameCard(gameTypeUiState: GameTypeUiState) {}
            override fun onClickStartGame() {}
        }
    )
}