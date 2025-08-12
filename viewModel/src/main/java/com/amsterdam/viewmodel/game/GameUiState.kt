package com.amsterdam.viewmodel.game

import com.amsterdam.domain.useCase.game.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
import com.amsterdam.domain.useCase.game.PosterGuessingGameQuestion
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
    val isLoading: Boolean = true,
    val score: Int = 0,
    val totalPointsPerQuestion: Int = 0
)

data class GameQuestionUiState(
    val questionData: String,
    val answers: List<String>,
    val correctAnswer: String,
)

fun PosterGuessingGameQuestion.toGameQuestionUiState(): GameQuestionUiState {
    return GameQuestionUiState(
        questionData = this.posterUrl,
        answers = this.movieChoices,
        correctAnswer = this.correctMovieName
    )
}

fun MovieReleasedDateQuestion.toGameQuestionUiState(): GameQuestionUiState {
    return GameQuestionUiState(
        questionData = this.question,
        answers = this.releaseYearChoices.map { it.toString() },
        correctAnswer = this.correctChoice.toString()
    )
}

fun GameQuestionUiState.toMovieReleasedDateQuestion(): MovieReleasedDateQuestion {
    return MovieReleasedDateQuestion(
        question = this.questionData,
        releaseYearChoices = this.answers.map { it.toInt() },
        correctChoice = this.correctAnswer.toInt()
    )
}