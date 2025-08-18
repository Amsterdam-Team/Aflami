package com.amsterdam.viewmodel.guessReleseDateGame

interface GuessReleaseYearInteractionListener {
    fun onHintClicked()
    fun onSelectAnswer(selectedAnswerIndex : Int)
    fun onMoveToNextQuestion()
    fun onCloseButtonClicked()
    fun dismissNotEnoughPointsDialog()
    fun onClickClose()
    fun onClickRetryLoading()
}