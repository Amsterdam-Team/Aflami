package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.entity.GameDifficulty

class SubmitGuessMovieByPosterAnswerUseCase(
    private val getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {

    suspend operator fun invoke(
        question: MoviePosterQuestion,
        answer: String,
        difficultyType: GameDifficulty.DifficultyType
    ): AnswerResult {
        val gameDifficulty = getDifficulty(difficultyType)
        val correct = question.correctMovieName == answer
        var earnedPoints = 0
        if (correct) {
            updatePoints(gameDifficulty.pointsPerQuestion)
             earnedPoints = gameDifficulty.pointsPerQuestion

        }

        return AnswerResult(correct,earnedPoints)
    }

    data class AnswerResult(
        val isCorrect: Boolean,
        val earnedPoints: Int
    )
}
