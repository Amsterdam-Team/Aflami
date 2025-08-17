package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository

class GetCollectedPointsUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(gameSessionId: Long): Int {
        return gameRepository.getCollectedPoints(gameSessionId)
    }
}