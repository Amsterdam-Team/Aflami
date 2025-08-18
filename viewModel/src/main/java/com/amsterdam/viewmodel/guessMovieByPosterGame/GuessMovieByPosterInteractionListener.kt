package com.amsterdam.viewmodel.guessMovieByPosterGame

interface GuessMovieByPosterInteractionListener {
    fun onHintClicked()
    fun onSelectAnswer(selectedAnswerIndex: Int)
    fun onMoveToNextQuestion()
    fun dismissNotEnoughPointsDialog()
    fun onCloseButtonClicked()
    fun onClickRetryLoading()
}