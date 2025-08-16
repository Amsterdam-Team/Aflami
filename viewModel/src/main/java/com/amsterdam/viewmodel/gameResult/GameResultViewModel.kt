package com.amsterdam.viewmodel.gameResult

import com.amsterdam.domain.useCase.game.GetCollectedPointsUseCase
import com.amsterdam.domain.useCase.game.GetSpentSecondsUseCase
import com.amsterdam.entity.Game.GameType
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.viewmodel.gameResult.ResultSideEffect.GameTypeUi
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameResultViewModel @Inject constructor(
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
            it.copy(points = getCollectedPointsUseCase(gameSessionId), timeInSeconds = getSpentSecondsUseCase(gameSessionId))
        }
    }

    override fun onClickPlayAgain() {
        val gameTypeUi = when(gameType) {
            GameType.GUESS_CHARACTER -> GameTypeUi.GUESS_CHARACTER
            GameType.GUESS_MOVIE_BY_POSTER -> GameTypeUi.GUESS_MOVIE_BY_POSTER
            GameType.GUESS_MOVIE_BY_RELEASE -> GameTypeUi.GUESS_RELEASE_YEAR
            GameType.GUESS_MOVIE_BY_GENRE -> GameTypeUi.GUESS_GENRE
        }
        sendNewNavigationEffect(
            ResultSideEffect.NavigateToGame(
                gameTypeUi,
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