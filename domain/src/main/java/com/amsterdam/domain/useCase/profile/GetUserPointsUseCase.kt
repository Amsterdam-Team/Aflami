package com.amsterdam.domain.useCase.profile

import com.amsterdam.domain.repository.GamePointsRepository
import kotlinx.coroutines.flow.Flow

class GetUserPointsUseCase(
    private val gameRepository: GamePointsRepository
) {
    operator fun invoke(): Flow<Int> {
        return gameRepository.getPoints()
    }
}