package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GamePointsRepository
import kotlinx.coroutines.flow.first

class AddGamePointsUseCase(
    private val gameRepository: GamePointsRepository
) {
    suspend operator fun invoke(points: Int) {
        val points = gameRepository.getPoints().first().plus(points)
        gameRepository.updatePoints(points)
    }
}