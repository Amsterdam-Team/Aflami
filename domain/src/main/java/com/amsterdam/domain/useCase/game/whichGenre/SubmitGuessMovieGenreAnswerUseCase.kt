package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.useCase.game.whichGenre.GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.entity.category.MovieGenre

class SubmitGuessMovieGenreAnswerUseCase(
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val updateUserGamePointsUseCase: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        question: MovieGenreQuestion,
        answer: MovieGenre,
        difficultyType: DifficultyType
    ): AnswerResult {
        val gameDifficulty = getGameDifficultyUseCase(difficultyType)
        val correct = question.correctChoice == answer
        var earnedPoints = 0
        if (question.correctChoice == answer){
            updateUserGamePointsUseCase(gameDifficulty.pointsPerQuestion)
            earnedPoints = gameDifficulty.pointsPerQuestion
        }

        return AnswerResult(correct, earnedPoints)
    }

    data class AnswerResult(
        val isCorrect: Boolean,
        val earnedPoints: Int
    )
}