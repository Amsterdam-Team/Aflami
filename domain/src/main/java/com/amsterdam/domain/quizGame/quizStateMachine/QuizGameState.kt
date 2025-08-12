package com.amsterdam.domain.quizGame.quizStateMachine


abstract class QuizGameState(){
    open fun enter() {}

    open fun exit() {}

    abstract fun onEvent()

    fun transitionTo(state: QuizGameState){

    }
}