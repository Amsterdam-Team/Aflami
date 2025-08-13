package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.useCase.game.releaseYear.GenerateMovieReleaseYearQuestionsUseCase
import com.amsterdam.entity.GameDifficulty

class GuessCharacterGameUseCase(
    private val getGameData: GenerateMovieReleaseYearQuestionsUseCase,
    private val doHint: DoGuessCharacterGameHintUseCase,
    private val submitAnswer: SubmitCharacterAnswerUseCase
) {
    suspend fun startGame(difficultyType: GameDifficulty.DifficultyType) =
        getGameData(difficultyType)

    suspend fun giveHint(question: GenerateCharacterQuestionsUseCase.CharacterDataQuestion) =
        doHint(question)

    suspend fun answer(
        question: GenerateCharacterQuestionsUseCase.CharacterDataQuestion,
        answer: String,
        difficultyType: GameDifficulty.DifficultyType
    ) = submitAnswer(question, answer, difficultyType)
}