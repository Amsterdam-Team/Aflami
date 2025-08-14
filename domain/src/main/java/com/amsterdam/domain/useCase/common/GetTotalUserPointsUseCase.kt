package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.repository.GameRepository


class GetTotalUserPointsUseCase (private val gameRepository : GameRepository) {
     operator fun invoke()  = gameRepository.getUserPoints()
}