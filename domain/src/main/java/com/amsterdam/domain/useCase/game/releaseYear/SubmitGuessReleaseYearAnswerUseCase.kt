package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty

class SubmitGuessReleaseYearAnswerUseCase(
    private val getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        question: GameQuestion<Int>,
        answer: Int,
        difficultyType: GameDifficulty.DifficultyType
    ): AnswerResult {
        val gameDifficulty = getDifficulty(difficultyType)
        val correct = question.correctChoice == answer
        var earnedPoints = 0
        if (correct) {
            updatePoints(gameDifficulty.pointsPerQuestion)
            earnedPoints = gameDifficulty.pointsPerQuestion
        }

        return AnswerResult(correct, earnedPoints)
    }
}