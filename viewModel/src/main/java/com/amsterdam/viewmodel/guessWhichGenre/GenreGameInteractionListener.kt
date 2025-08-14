package com.amsterdam.viewmodel.guessWhichGenre

interface GenreGameInteractionListener {
    fun onCancelGameClick()
    fun onUseHint()
    fun onChooseAnswerClick(answerIndex: Int)
    fun dismissNotEnoughPointsDialog()
    fun onMoveToNextQuestion()
}
