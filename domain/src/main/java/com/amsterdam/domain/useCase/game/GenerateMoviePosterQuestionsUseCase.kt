package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.repository.PosterGuessingGameQuestion

class GenerateMoviePosterQuestionsUseCase(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(questionCount: Int): List<PosterGuessingGameQuestion> {
        return gameRepository.getPosterGuessingGameQuestions(questionCount)
    }
}