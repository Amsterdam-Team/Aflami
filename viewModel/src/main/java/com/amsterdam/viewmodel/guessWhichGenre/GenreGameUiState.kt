package com.amsterdam.viewmodel.guessWhichGenre

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.sharedGame.TimerUiState

data class GenreGameUiState(
    val isLoading: Boolean = false,
    val questions: List<GameQuestionUiState> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val isHintEnabled: Boolean = true,
    val isNotEnoughPointsDialogVisible: Boolean = false,
    val timerUiState: TimerUiState = TimerUiState(),
    val isNextEnabled: Boolean = false,
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect: Boolean? = null,
    val error: AflamiException? = null
)

data class GameQuestionUiState(
    val id: Long,
    val questionData: String,
    val answers: List<MovieGenre>,
    val correctAnswer: MovieGenre,
    val questionTime: Int = 0
)
