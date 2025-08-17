package com.amsterdam.domain.useCase.game

import kotlinx.datetime.Clock

class CreateGameSessionIdUseCase() {

     operator fun invoke()  = Clock.System.now().toEpochMilliseconds()
}