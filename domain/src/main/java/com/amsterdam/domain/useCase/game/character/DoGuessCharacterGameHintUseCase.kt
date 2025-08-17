package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.GameQuestion
import kotlinx.coroutines.flow.first

class DoGuessCharacterGameHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }

    suspend operator fun invoke(
        characterQuestion: GameQuestion<String>
    ): GameQuestion<String> {

        val currentPoints = getTotalUserPointsUseCase().first()

        if (currentPoints < REQUIRED_HINT_POINTS)
            throw NotEnoughPointsException()

        updatePoints(-REQUIRED_HINT_POINTS)

        return characterQuestion
    }
}
