package com.amsterdam.viewmodel.letsPlay

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.game.GetAvailableGamesUseCase
import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState
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
                selectedGameTypeUiState = null,
                selectedDifficultyLevel = null,
                isStartGameButtonEnable = false
            )
        }
    }

    override fun onClickGameCard(gameTypeUiState: GameTypeUiState) {
        updateState {
            it.copy(
                selectedGameTypeUiState = gameTypeUiState
            )
        }
    }

    override fun onClickStartGame() {
        val difficultyLevelName =
            state.value.selectedDifficultyLevel?.difficultyLevel?.name ?: return
        val navigateEffect = when (state.value.selectedGameTypeUiState) {
            GameTypeUiState.GUESS_CHARACTER ->
                LetsPlayEffect.NavigateToGuessCharacterScreen(difficultyLevelName)

            GameTypeUiState.GUESS_MOVIE_BY_POSTER ->
                LetsPlayEffect.NavigateToGuessMovieByPosterScreen(difficultyLevelName)

            GameTypeUiState.GUESS_MOVIE_BY_RELEASE ->
                LetsPlayEffect.NavigateToGuessMovieByReleaseScreen(difficultyLevelName)

            GameTypeUiState.GUESS_MOVIE_BY_GENRE ->
                LetsPlayEffect.NavigateToGuessMovieByGenreScreen(difficultyLevelName)

            null -> {
                return
            }
        }
        updateState {
            it.copy(
                selectedGameTypeUiState = null,
                selectedDifficultyLevel = null,
                isStartGameButtonEnable = false
            )
        }
        sendNewNavigationEffect(navigateEffect)
    }

}