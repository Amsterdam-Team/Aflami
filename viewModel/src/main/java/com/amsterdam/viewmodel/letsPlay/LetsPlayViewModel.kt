package com.amsterdam.viewmodel.letsPlay

import com.amsterdam.domain.useCase.game.GetAvailableGamesUseCase
import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
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
        updateState { getAvailableGamesUseCase().toLetsPlayUiState() }
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
        updateState { it.copy(selectedGameTypeUiState = null, selectedDifficultyLevel = null) }
    }

    override fun onClickGameCard(gameTypeUiState: GameTypeUiState) {
        updateState {
            it.copy(
                selectedGameTypeUiState = gameTypeUiState
            )
        }
    }

    override fun onClickStartGame() {
        updateState { it.copy(selectedGameTypeUiState = null) }
        sendNewEffect(LetsPlayEffect.NavigateToGameScreen)
    }

}