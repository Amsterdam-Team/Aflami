package com.amsterdam.domain.quizGame.quizStateMachine

import com.amsterdam.domain.quizGame.strategies.QuestionProvider

class LoadingQuizState(
    private val questionProvider: QuestionProvider
): QuizGameState() {
    override fun onEvent() {
        TODO("Not yet implemented")
    }

    override fun enter() {
        super.enter()
        getQuestions()
    }

    private fun getQuestions(){
        transitionTo()
    }
}