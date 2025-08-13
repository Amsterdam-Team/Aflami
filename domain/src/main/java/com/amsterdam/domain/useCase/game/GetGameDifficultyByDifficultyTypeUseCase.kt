package com.amsterdam.domain.useCase.game

import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameDifficulty.DifficultyType

class GetGameDifficultyByDifficultyTypeUseCase() {

    operator fun invoke(difficultyType: DifficultyType): GameDifficulty {
        return when (difficultyType) {
            DifficultyType.EASY -> GameDifficulty(
                totalQuestions = 5,
                timeLimitSeconds = 45,
                pointsPerQuestion = 5,
                difficultyType = DifficultyType.EASY
            )

            DifficultyType.MEDIUM -> GameDifficulty(
                totalQuestions = 10,
                timeLimitSeconds = 30,
                pointsPerQuestion = 10,
                difficultyType = DifficultyType.MEDIUM
            )

            DifficultyType.HARD -> GameDifficulty(
                totalQuestions = 20,
                timeLimitSeconds = 10,
                pointsPerQuestion = 20,
                difficultyType = DifficultyType.HARD
            )
        }
    }
}