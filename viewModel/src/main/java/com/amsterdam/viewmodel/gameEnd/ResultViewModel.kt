package com.amsterdam.viewmodel.gameEnd

import com.amsterdam.entity.GameDifficulty
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ResultViewModel @Inject constructor(
    gameResultArgs: GameResultArgs,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ResultUiState, ResultSideEffect>(
    initialState = ResultUiState(),
    dispatcherProvider = dispatcherProvider
), ResultInteractionListener {

    private val gameType = ResultSideEffect.GameType.valueOf(gameResultArgs.gameType)
    private val difficultyType =
        GameDifficulty.DifficultyType.valueOf(gameResultArgs.gameDifficulty)

    init {
        val points = gameResultArgs.totalCollectedPoints
        val time = gameResultArgs.totalSpentSeconds

        updateState {
            it.copy(points = points, timeInSeconds = time)
        }
    }

    override fun onClickPlayAgain() {
        val gameDifficulty = when (difficultyType) {
            GameDifficulty.DifficultyType.EASY -> GameDifficulty(
                totalQuestions = 10,
                timeLimitSeconds = 60,
                pointsPerQuestion = 10,
                difficultyType = difficultyType
            )

            GameDifficulty.DifficultyType.MEDIUM -> GameDifficulty(
                totalQuestions = 15,
                timeLimitSeconds = 90,
                pointsPerQuestion = 15,
                difficultyType = difficultyType
            )

            GameDifficulty.DifficultyType.HARD -> GameDifficulty(
                totalQuestions = 20,
                timeLimitSeconds = 120,
                pointsPerQuestion = 20,
                difficultyType = difficultyType
            )
        }

        sendNewNavigationEffect(
            ResultSideEffect.NavigateToGame(
                gameType = gameType,
                difficulty = gameDifficulty,
                totalCollectedPoints = 0,
                totalSpentSeconds = 0
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