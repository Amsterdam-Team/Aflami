package com.amsterdam.ui.screens.letsPlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.entity.Game
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.application.LocalScaffoldBottomPadding
import com.amsterdam.ui.screens.letsPlay.component.GameCard
import com.amsterdam.ui.screens.letsPlay.component.DifficultyLevelDialog
import com.amsterdam.ui.screens.letsPlay.component.PlayScreenAppBar
import com.amsterdam.viewmodel.letsPlay.LetsPlayEffect
import com.amsterdam.viewmodel.letsPlay.LetsPlayInteractionListener
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayViewModel

@Composable
fun LetsPlayScreen(viewModel: LetsPlayViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LetsPlayEffect.NavigateToGuessMovieByReleaseScreen -> navigationManager.toGuessReleaseYearGame(
                    effect.difficulty
                )

                is LetsPlayEffect.NavigateToGuessCharacterScreen -> navigationManager.toGuessCharacter(
                    effect.difficulty
                )

                is LetsPlayEffect.NavigateToGuessMovieByPosterScreen -> navigationManager.toGuessMovieByPosterGame(
                    effect.difficulty
                )

                is LetsPlayEffect.NavigateToGuessMovieByGenreScreen -> navigationManager.toGenreGame(
                    effect.difficulty
                )
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets(bottom = LocalScaffoldBottomPadding.current))
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            columns = GridCells.Adaptive(328.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            stickyHeader {
                PlayScreenAppBar(
                    totalScore = state.totalUserPoint,
                    modifier = Modifier
                        .background(AppTheme.color.surface)
                        .padding(horizontal = 16.dp)
                )
            }

            this.items(state.games) {
                val gameCardData = it.gameType.getGameTypeData()
                GameCard(
                    title = stringResource(gameCardData.title),
                    description = stringResource(gameCardData.description),
                    containerColor = gameCardData.containerColor,
                    borderColors = gameCardData.borderColors,
                    shadowColor = gameCardData.shadowColor,
                    onClick = { interactionListener.onClickGameCard(it.gameType) },
                    gameCardImageContentType = gameCardData.gameCardImageContentType,
                    isPlayable = state.totalUserPoint >= it.requiredPoints,
                    unlockPrice = "${it.requiredPoints}"
                )
            }
        }



    AnimatedVisibility(
        visible = state.selectedGameType != null,
        modifier = Modifier.align(Alignment.Center),
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
        state = LetsPlayUiState(), interactionListener = object : LetsPlayInteractionListener {
            override fun onSelectDifficultyLevel(difficultyLevel: GameDifficultyUiState) {}
            override fun onClickCloseDifficultyLevelDialog() {}
            override fun onClickGameCard(gameType: Game.GameType) {}
            override fun onClickStartGame() {}
        })
}