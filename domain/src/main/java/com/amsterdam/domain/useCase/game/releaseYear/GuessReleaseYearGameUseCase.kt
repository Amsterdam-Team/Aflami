package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.entity.GameDifficulty

class GuessReleaseYearGameUseCase(
    private val getGameData: GenerateMovieReleaseYearQuestionsUseCase,
    private val doHint: DoGuessReleaseGameHintUseCase,
    private val submitAnswer: SubmitGuessReleaseYearAnswerUseCase
) {
    suspend fun startGame(difficultyType: GameDifficulty.DifficultyType) =
        getGameData(difficultyType)

    suspend fun giveHint(question: GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion) =
        doHint(question)

    suspend fun answer(
        question: GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion,
        answer: Int,
        difficultyType: GameDifficulty.DifficultyType
    ) = submitAnswer(question, answer, difficultyType)
}