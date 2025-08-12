package com.amsterdam.domain.quizGame.gameManager

import com.amsterdam.domain.quizGame.CurrentQuizData
import com.amsterdam.domain.quizGame.QuizEvent
import com.amsterdam.domain.quizGame.quizStateMachine.QuizGameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QuizOrchestrator(
): QuizManager {

    override val state: StateFlow<CurrentQuizData> = MutableStateFlow(CurrentQuizData())

    override fun onStateTransition(currentState: QuizGameState, nextState: CurrentQuizData) {
        TODO("Not yet implemented")
    }

    override fun onEvent(event: QuizEvent) {
        TODO("Not yet implemented")
    }
}