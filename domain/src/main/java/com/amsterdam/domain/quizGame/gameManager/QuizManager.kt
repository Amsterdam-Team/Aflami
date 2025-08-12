package com.amsterdam.domain.quizGame.gameManager

import com.amsterdam.domain.quizGame.CurrentQuizData
import com.amsterdam.domain.quizGame.QuizEvent
import com.amsterdam.domain.quizGame.quizStateMachine.QuizGameState
import kotlinx.coroutines.flow.StateFlow

interface QuizManager {
    val state: StateFlow<CurrentQuizData>

    fun onStateTransition(currentState: QuizGameState, nextState: CurrentQuizData)

    fun onEvent(event: QuizEvent)
}
