package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.entity.category.MovieGenre

class GenerateMovieGenreQuestionsUseCase(
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<GameQuestion<MovieGenre>> {
        val questionsCounts =  getGameDifficultyUseCase(difficultyType).totalQuestions
        val questionTime = getGameDifficultyUseCase(difficultyType).timeLimitSeconds
        val movies = gameRepository.getRandomMoviesWithNotNullDate(questionsCounts)

        return movies.map { movie ->
            val correctGenre = movie.categories.random()
            val wrongGenres =
                MovieGenre
                    .entries
                    .drop(1)
                    .filter { !(movie.categories.contains(it)) }
                    .shuffled()
                    .take(WRONG_ANSWER_COUNT)
            val choices = (listOf(correctGenre) + wrongGenres).shuffled()

            GameQuestion(
                question = movie.name,
                choices = choices,
                correctChoice = correctGenre,
                questionTime = questionTime
            )
        }
    }

    private companion object {
        const val WRONG_ANSWER_COUNT = 3
    }
}