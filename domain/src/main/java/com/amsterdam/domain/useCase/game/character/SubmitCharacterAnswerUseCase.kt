package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.entity.GameDifficulty

class SubmitCharacterAnswerUseCase(
    private val getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        question: GenerateCharacterQuestionsUseCase.CharacterDataQuestion,
        answer: String,
        difficultyType: GameDifficulty.DifficultyType
    ): AnswerResult {
        val gameDifficulty = getDifficulty(difficultyType)
        val correct = question.correctAnswer == answer
        if (correct) {
            updatePoints(gameDifficulty.pointsPerQuestion)
        }
        updatePoints(gameDifficulty.pointsPerQuestion)

        return AnswerResult(correct, gameDifficulty.pointsPerQuestion)
    }

    data class AnswerResult(
        val isCorrect: Boolean,
        val earnedPoints: Int
    )
}