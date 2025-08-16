package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.GameQuestion
import kotlinx.coroutines.flow.first

class DoGuessReleaseGameHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }

    suspend operator fun invoke(
        movieReleasedDateQuestion: GameQuestion<Int>,
    ): GameQuestion<Int> {

        val currentPoints = getTotalUserPointsUseCase().first()

        if (currentPoints < REQUIRED_HINT_POINTS)
            throw NotEnoughPointsException()

        val choices = movieReleasedDateQuestion.choices.toMutableList()

        val wrongChoiceToRemove = choices
            .filter { it != movieReleasedDateQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)
        updatePoints(-REQUIRED_HINT_POINTS)
        return movieReleasedDateQuestion.copy(choices = choices)
    }
}
