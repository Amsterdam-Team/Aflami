package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.whichGenre.GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.category.MovieGenre

class GuessMovieGenreUseCase(
    private val generateMovieGenreQuestionsUseCase: GenerateMovieGenreQuestionsUseCase,
    private val submitGuessMovieGenreAnswerUseCase: SubmitGuessMovieGenreAnswerUseCase,
    private val doGuessGenreGameHintUseCase: DoGuessGenreGameHintUseCase
) {
    suspend fun startGame(difficultyType: GameDifficulty.DifficultyType) =
        generateMovieGenreQuestionsUseCase(difficultyType)

    suspend fun giveHint(question: MovieGenreQuestion) = doGuessGenreGameHintUseCase(question)

    suspend fun checkAnswer(
        question: MovieGenreQuestion,
        answer: MovieGenre,
        difficultyType: GameDifficulty.DifficultyType
    ) = submitGuessMovieGenreAnswerUseCase(question, answer, difficultyType)
}
