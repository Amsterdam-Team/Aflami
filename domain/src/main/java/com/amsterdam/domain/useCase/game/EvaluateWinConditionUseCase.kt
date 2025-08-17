package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository

class EvaluateWinConditionUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(gameSessionId: Long): Boolean {
        return gameRepository.getCollectedPoints(gameSessionId) > 0
    }
}