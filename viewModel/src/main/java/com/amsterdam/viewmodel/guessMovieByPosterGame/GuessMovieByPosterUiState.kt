package com.amsterdam.viewmodel.guessMovieByPosterGame

import com.amsterdam.domain.useCase.game.guessByPoster.MoviePosterQuestion
import com.amsterdam.viewmodel.sharedGame.TimerUiState

data class GuessMovieByPosterUiState(
    val isLoading: Boolean = true,
    val totalCollectedPoints: Int = 0,
    val questions: List<QuestionUiState> = emptyList(),
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect: Boolean? = null,
    val isHintEnabled: Boolean = true,
    val isNotEnoughPointsDialogVisible: Boolean = false,
    val isNextEnabled: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val timerUiState: TimerUiState = TimerUiState()
) {
    data class QuestionUiState(
        val posterUrl: String = "",
        val movieNameChoices: List<String> = emptyList(),
        val correctAnswer: String = "",
        val questionTimeSeconds: Int = 0,
        val blurRadius: Int = 8
    )
}

fun MoviePosterQuestion.toQuestionUiState(): GuessMovieByPosterUiState.QuestionUiState {
    return GuessMovieByPosterUiState.QuestionUiState(
        posterUrl = this.posterUrl,
        movieNameChoices = this.movieNameChoices,
        correctAnswer = this.correctMovieName,
        questionTimeSeconds = this.questionTimeSeconds
    )
}

fun GuessMovieByPosterUiState.QuestionUiState.toMoviePosterQuestion(): MoviePosterQuestion {
    return MoviePosterQuestion(
        posterUrl = this.posterUrl,
        movieNameChoices = this.movieNameChoices,
        correctMovieName = this.correctAnswer,
        questionTimeSeconds = this.questionTimeSeconds
    )
}

fun List<MoviePosterQuestion>.toQuestionsUiState(): List<GuessMovieByPosterUiState.QuestionUiState> =
    map { it.toQuestionUiState() }