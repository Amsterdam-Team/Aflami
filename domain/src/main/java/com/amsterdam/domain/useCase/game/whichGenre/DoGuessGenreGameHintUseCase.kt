package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.category.MovieGenre
import kotlinx.coroutines.flow.first

class DoGuessGenreGameHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updateUserPointsUseCase: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(movieGenreQuestion: GameQuestion<MovieGenre>): GameQuestion<MovieGenre> {
        val currentPoints = getTotalUserPointsUseCase().first()
        if(currentPoints < REQUIRED_HINT_POINTS) throw NotEnoughPointsException()

        val choices = movieGenreQuestion.choices.toMutableList()
        val wrongChoiceToRemove = choices
            .filter { it != movieGenreQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)
        updateUserPointsUseCase(-REQUIRED_HINT_POINTS)
        return movieGenreQuestion.copy(choices = choices)
    }

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }
}