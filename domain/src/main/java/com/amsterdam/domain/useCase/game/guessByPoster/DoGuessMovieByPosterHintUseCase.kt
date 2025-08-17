package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.GameQuestion
import kotlinx.coroutines.flow.first

class DoGuessMovieByPosterHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        moviePosterQuestion: GameQuestion<String>
    ): GameQuestion<String> {

        val currentPoints = getTotalUserPointsUseCase().first()

        if (currentPoints < REQUIRED_HINT_POINTS) {
            throw NotEnoughPointsException()
        }

        updatePoints(-REQUIRED_HINT_POINTS)

        return moviePosterQuestion
    }

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }
}