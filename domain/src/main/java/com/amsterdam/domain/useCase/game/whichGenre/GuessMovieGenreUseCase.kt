package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.category.MovieGenre

class GuessMovieGenreUseCase(
    private val generateMovieGenreQuestionsUseCase: GenerateMovieGenreQuestionsUseCase,
    private val submitGuessMovieGenreAnswerUseCase: SubmitGuessMovieGenreAnswerUseCase,
    private val doGuessGenreGameHintUseCase: DoGuessGenreGameHintUseCase
) {
    suspend fun startGame(difficultyType: GameDifficulty.DifficultyType) =
        generateMovieGenreQuestionsUseCase(difficultyType)

    suspend fun giveHint(question: GameQuestion<MovieGenre>) = doGuessGenreGameHintUseCase(question)

    suspend fun checkAnswer(
        question: GameQuestion<MovieGenre>,
        answer: MovieGenre,
        difficultyType: GameDifficulty.DifficultyType
    ) = submitGuessMovieGenreAnswerUseCase(question, answer, difficultyType)
}
