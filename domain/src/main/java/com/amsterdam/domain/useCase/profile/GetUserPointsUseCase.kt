package com.amsterdam.domain.useCase.profile

import com.amsterdam.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class GetUserPointsUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(): Flow<Int> {
        return gameRepository.getUserPoints()
    }
}