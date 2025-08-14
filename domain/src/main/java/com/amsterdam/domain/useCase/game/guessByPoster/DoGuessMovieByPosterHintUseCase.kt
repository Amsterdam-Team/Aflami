package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import kotlinx.coroutines.flow.first

class DoGuessMovieByPosterHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        moviePosterQuestion: MoviePosterQuestion
    ): MoviePosterQuestion {

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