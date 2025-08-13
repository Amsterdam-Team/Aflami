package com.amsterdam.viewmodel.guessCharacterGame

interface GuessCharacterInteractionListener {
    fun onHintClicked()
    fun onSelectAnswer(selectedAnswerIndex : Int)
    fun onMoveToNextQuestion()
    fun onCloseButtonClicked()
}