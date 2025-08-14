package com.amsterdam.viewmodel.game.whichGenre

interface GenreGameInteractionListener {
    fun onCancelGameClick()
    fun onUseHint()
    fun onChooseAnswerClick(answerIndex: Int)
    fun dismissNotEnoughPointsDialog()
    fun onMoveToNextQuestion()
}
