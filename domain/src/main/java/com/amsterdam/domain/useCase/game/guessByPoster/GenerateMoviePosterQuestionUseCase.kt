package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty


class GenerateMoviePosterQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase
) {

    suspend operator fun invoke(difficultyType: GameDifficulty.DifficultyType): List<MoviePosterQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)

        val movies = gameRepository.getRandomMoviesWithNotNullPoster(gameDifficulty.totalQuestions * 4)

        return movies.chunked(4).map { group ->
            val correctMovie = group.random()
            val wrongChoices = group.filter { it != correctMovie }.map { it.name }

            val movieNameChoices = (wrongChoices + correctMovie.name).shuffled()

            MoviePosterQuestion(
                posterUrl = correctMovie.posterUrl,
                movieNameChoices = movieNameChoices,
                correctMovieName = correctMovie.name,
                questionTimeSeconds = gameDifficulty.timeLimitSeconds
            )
        }
    }
}

data class MoviePosterQuestion(
    val posterUrl: String,
    val movieNameChoices: List<String>,
    val correctMovieName: String,
    val questionTimeSeconds: Int
)
