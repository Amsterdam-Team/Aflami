package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository

class AddPointsToGameUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(points: Int, gameSessionId: Long) {
        gameRepository.addPointsToGame(points, gameSessionId)
    }
}