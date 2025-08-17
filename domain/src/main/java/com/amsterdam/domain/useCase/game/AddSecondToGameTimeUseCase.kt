package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository

class AddSecondToGameTimeUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(gameSessionId: Long) {
        gameRepository.addOneSecondToGameTime(gameSessionId)
    }
}