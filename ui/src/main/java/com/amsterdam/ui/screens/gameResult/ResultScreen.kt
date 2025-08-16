package com.amsterdam.ui.screens.gameResult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.components.Scaffold
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.entity.Game
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.screens.gameResult.component.CompletionCard
import com.amsterdam.ui.screens.gameResult.component.GameResultAppBar
import com.amsterdam.ui.screens.gameResult.component.StatCard
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.viewmodel.gameResult.ResultInteractionListener
import com.amsterdam.viewmodel.gameResult.ResultSideEffect
import com.amsterdam.viewmodel.gameResult.ResultUiState
import com.amsterdam.viewmodel.gameResult.GameResultViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ResultScreen(
    viewModel: GameResultViewModel = hiltViewModel()

) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigationManager = LocalNavManager.current
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ResultSideEffect.NavigateToGame -> {
                    when (effect.gameType) {
                        Game.GameType.GUESS_MOVIE_BY_POSTER -> {
                            navigationManager.toGuessMovieByPosterGame(effect.difficultyType)
                        }

                        Game.GameType.GUESS_RELEASE_YEAR -> {
                            navigationManager.toGuessReleaseYearGame(effect.difficultyType)
                        }

                        Game.GameType.GUESS_CHARACTER -> {
                            navigationManager.toGuessCharacter(effect.difficultyType)
                        }

                        Game.GameType.GUESS_GENRE -> {
                            navigationManager.toGenreGame(effect.difficultyType)
                        }
                    }
                }

                is ResultSideEffect.NavigateBackToMenu -> navigationManager.navigateUp()

            }
        }
    }

    ResultScreenContent(
        state = state,
        listener = viewModel
    )
}

@Composable
fun ResultScreenContent(
    state: ResultUiState,
    listener: ResultInteractionListener
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ConfirmButton(
                    title = stringResource(R.string.back_to_menue),
                    onClick = listener::onClickBackToMenu,
                    isEnabled = true,
                    isLoading = false,
                    isNegative = false,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(
                    title = stringResource(R.string.play_again),
                    onClick = listener::onClickPlayAgain,
                    isEnabled = true,
                    isLoading = false,
                    isNegative = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                GameResultAppBar(onCloseClicked = listener::onClickClose)

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CompletionCard()
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            iconRes = R.drawable.img_user_rating,
                            label = "Points Achieved",
                            value = "${state.points} Pts."
                        )

                        StatCard(
                            modifier = Modifier.weight(1f),
                            iconRes = R.drawable.img_user_history,
                            label = "Total time",
                            value = "${state.timeInSeconds} Sec."
                        )
                    }
                }
            }
        }
    }
}


@ThemeAndLocalePreviews
@Composable
private fun ResultScreenPreview() {
    ResultScreenContent(
        state = ResultUiState(points = 110, timeInSeconds = 110),
        listener = object : ResultInteractionListener {
            override fun onClickPlayAgain() {}
            override fun onClickBackToMenu() {}
            override fun onClickClose() {}
        }
    )
}