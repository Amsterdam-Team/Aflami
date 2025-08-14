package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.domain.useCase.game.releaseYear.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState.QuestionUiState

fun MovieReleasedDateQuestion.toQuestionUiStateUiState(): QuestionUiState {
    return QuestionUiState(
        movieName = this.question,
        releaseYearAnswer = this.releaseYearChoices.map { it.toString()},
        correctAnswer = this.correctChoice.toString(),
        questionTimeSeconds = questionTimeSeconds
    )
}

fun QuestionUiState.toMovieReleasedDateQuestion(): MovieReleasedDateQuestion {
    return MovieReleasedDateQuestion(
        question = this.movieName,
        releaseYearChoices = this.releaseYearAnswer.map { it.toInt() },
        correctChoice = this.correctAnswer.toInt(),
        questionTimeSeconds = questionTimeSeconds
    )
}

fun List<MovieReleasedDateQuestion>.toQuestionsUiStateUiState(): List<QuestionUiState>  = map { it.toQuestionUiStateUiState() }