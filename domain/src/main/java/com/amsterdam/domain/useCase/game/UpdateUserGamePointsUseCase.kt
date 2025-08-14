package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository
import kotlinx.coroutines.flow.first

class UpdateUserGamePointsUseCase (private val gameRepository : GameRepository) {
    suspend operator fun invoke(points: Int) {
        val points = gameRepository.getUserPoints().first().plus(points)
        gameRepository.updatePoints(points)
    }
}