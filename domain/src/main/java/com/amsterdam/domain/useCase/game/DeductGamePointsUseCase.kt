package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.repository.GamePointsRepository
import kotlinx.coroutines.flow.first

class DeductGamePointsUseCase(
    private val gameRepository: GamePointsRepository
) {
    suspend operator fun invoke(points: Int) {
        val currentPoints = gameRepository.getPoints().first().takeIf { it >= points }
            ?: throw NotEnoughPointsException()

        gameRepository.updatePoints(currentPoints.minus(points))
    }
}