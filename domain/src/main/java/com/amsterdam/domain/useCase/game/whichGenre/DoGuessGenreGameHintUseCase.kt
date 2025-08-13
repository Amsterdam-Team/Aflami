package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.useCase.game.whichGenre.GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion
import kotlinx.coroutines.flow.first

class DoGuessGenreGameHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updateUserPointsUseCase: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(movieGenreQuestion: MovieGenreQuestion): MovieGenreQuestion {
        val currentPoints = getTotalUserPointsUseCase().first()
        if(currentPoints < REQUIRED_HINT_POINTS) throw NotEnoughPointsException()

        val choices = movieGenreQuestion.genreChoices.toMutableList()
        val wrongChoiceToRemove = choices
            .filter { it != movieGenreQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)
        updateUserPointsUseCase(-REQUIRED_HINT_POINTS)
        return movieGenreQuestion.copy(genreChoices = choices)
    }

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }
}