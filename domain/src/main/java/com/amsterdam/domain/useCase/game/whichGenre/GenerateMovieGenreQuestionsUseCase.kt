package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.entity.category.MovieGenre
import kotlin.uuid.ExperimentalUuidApi

class GenerateMovieGenreQuestionsUseCase(
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<MovieGenreQuestion> {
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

            MovieGenreQuestion(
                id = movie.id,
                question = movie.name,
                genreChoices = choices,
                correctChoice = correctGenre,
                questionTime = questionTime
            )
        }
    }

    data class MovieGenreQuestion(
        val id: Long,
        val question: String,
        val genreChoices: List<MovieGenre>,
        val correctChoice: MovieGenre,
        val questionTime: Int
    )

    private companion object {
        const val WRONG_ANSWER_COUNT = 3
    }
}