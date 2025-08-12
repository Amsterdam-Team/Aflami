package com.amsterdam.viewmodel.game

import com.amsterdam.entity.Game
import com.amsterdam.viewmodel.sharedGame.TimerUiState

data class GameUiState(
    val gameType: Game.GameType = Game.GameType.GUESS_MOVIE_BY_GENRE,
    val questions: List<GameQuestionUiState> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val isHintEnabled: Boolean = true,
    val timerUiState: TimerUiState = TimerUiState(),
    val isNextEnabled: Boolean = false,
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect: Boolean? = null,
)

data class GameQuestionUiState(
    val questionData: String,
    val answers: List<String>,
)
