package com.amsterdam.viewmodel.game

interface GameInteractionListener {
    fun onCancelGameClick()

    fun onChooseAnswerClick(answerIndex: Int)

    fun onUseHint()

    fun onMoveToNextQuestion()
}
