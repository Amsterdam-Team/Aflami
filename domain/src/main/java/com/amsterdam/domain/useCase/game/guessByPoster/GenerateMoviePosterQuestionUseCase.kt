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
        val movies = gameRepository.getRandomMoviesWithNotNullPoster(gameDifficulty.totalQuestions)

        return movies.mapIndexed { _, movie ->
            val correctMovieName = movie.name
            val wrongChoices = movies
                .filter { it.name != correctMovieName }
                .shuffled()
                .take(3)
                .map { it.name }

            val movieNameChoices = (wrongChoices + correctMovieName).shuffled()

            MoviePosterQuestion(
                posterUrl = movie.posterUrl,
                movieNameChoices = movieNameChoices,
                correctMovieName = correctMovieName,
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
