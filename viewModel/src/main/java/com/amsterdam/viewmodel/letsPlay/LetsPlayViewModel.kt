package com.amsterdam.viewmodel.letsPlay

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.GetAvailableGamesUseCase
import com.amsterdam.entity.Game
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetsPlayViewModel @Inject constructor(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val getAvailableGamesUseCase: GetAvailableGamesUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<LetsPlayUiState, LetsPlayEffect>(
    dispatcherProvider = dispatcherProvider,
    initialState = LetsPlayUiState()
), LetsPlayInteractionListener {


    init {
        getTotalUserPoints()
        updateState { getAvailableGamesUseCase().toLetsPlayUiState() }
    }

    private fun getTotalUserPoints() {
        tryToExecute(
            action = { getTotalUserPointsUseCase() },
            onSuccess = {
                viewModelScope.launch {
                    it.collect { totalPoints ->
                        updateState { it.copy(totalUserPoint = totalPoints) }
                    }
                }
            }
        )
    }

    override fun onSelectDifficultyLevel(difficultyLevel: GameDifficultyUiState) {
        updateState {
            it.copy(
                selectedDifficultyLevel = difficultyLevel,
                isStartGameButtonEnable = true
            )
        }
    }

    override fun onClickCloseDifficultyLevelDialog() {
        updateState {
            it.copy(
                selectedGameType = null,
                selectedDifficultyLevel = null,
                isStartGameButtonEnable = false
            )
        }
    }

    override fun onClickGameCard(gameType: Game.GameType) {
        updateState {
            it.copy(
                selectedGameType = gameType
            )
        }
    }

    override fun onClickStartGame() {
        val difficultyLevelName =
            state.value.selectedDifficultyLevel?.difficultyLevel?.name ?: return
        val navigateEffect = when (state.value.selectedGameType) {
            Game.GameType.GUESS_CHARACTER ->
                LetsPlayEffect.NavigateToGuessCharacterScreen(difficultyLevelName)

            Game.GameType.GUESS_MOVIE_BY_POSTER ->
                LetsPlayEffect.NavigateToGuessMovieByPosterScreen(difficultyLevelName)

            Game.GameType.GUESS_RELEASE_YEAR ->
                LetsPlayEffect.NavigateToGuessMovieByReleaseScreen(difficultyLevelName)

            Game.GameType.GUESS_GENRE ->
                LetsPlayEffect.NavigateToGuessMovieByGenreScreen(difficultyLevelName)

            null -> {
                return
            }
        }
        updateState {
            it.copy(
                selectedGameType = null,
                selectedDifficultyLevel = null,
                isStartGameButtonEnable = false
            )
        }
        sendNewNavigationEffect(navigateEffect)
    }

}