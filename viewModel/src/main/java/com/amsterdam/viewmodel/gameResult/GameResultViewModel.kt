package com.amsterdam.viewmodel.gameResult

import com.amsterdam.domain.useCase.game.EvaluateWinConditionUseCase
import com.amsterdam.domain.useCase.game.GetCollectedPointsUseCase
import com.amsterdam.domain.useCase.game.GetSpentSecondsUseCase
import com.amsterdam.entity.Game.GameType
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameResultViewModel @Inject constructor(
    checkGameWinUseCase: EvaluateWinConditionUseCase,
    getCollectedPointsUseCase: GetCollectedPointsUseCase,
    getSpentSecondsUseCase: GetSpentSecondsUseCase,
    gameResultArgs: GameResultArgs,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ResultUiState, ResultSideEffect>(
    initialState = ResultUiState(),
    dispatcherProvider = dispatcherProvider
), ResultInteractionListener {

    private val gameType = GameType.valueOf(gameResultArgs.gameType)
    private val difficultyType = DifficultyType.valueOf(gameResultArgs.gameDifficulty)
    private val gameSessionId = gameResultArgs.gameSessionId

    init {
        updateState {
            it.copy(
                gameType = gameType,
                points = getCollectedPointsUseCase(gameSessionId),
                timeInSeconds = getSpentSecondsUseCase(gameSessionId),
                isVictory = checkGameWinUseCase(gameSessionId)
            )
        }
    }

    override fun onClickPlayAgain() {
        sendNewNavigationEffect(
            ResultSideEffect.NavigateToGame(
                gameType,
                difficultyType.name
            )
        )
    }

    override fun onClickBackToMenu() {
        sendNewNavigationEffect(ResultSideEffect.NavigateBackToMenu)
    }

    override fun onClickClose() {
        onClickBackToMenu()
    }
}