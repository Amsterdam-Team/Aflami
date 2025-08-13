@file:OptIn(ExperimentalUuidApi::class)

package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.timer.TimerHandler
import kotlin.uuid.ExperimentalUuidApi


class GuessByCharacterGameUseCase(
    private val gameRepository: GameRepository,
    private val timerHandler: TimerHandler,
) {

}