package com.amsterdam.viewmodel.game.whichGenre

import com.amsterdam.entity.category.MovieGenre

interface GenreGameInteractionListener {
    fun onCancelGameClick()
    fun onUseHint()
    fun onChooseAnswerClick(answer: MovieGenre, answerIndex: Int)
    fun onMoveToNextQuestion()
}
