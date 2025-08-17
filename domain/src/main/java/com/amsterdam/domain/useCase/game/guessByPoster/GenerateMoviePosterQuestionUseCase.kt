package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty


class GenerateMoviePosterQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase
) {

    suspend operator fun invoke(difficultyType: GameDifficulty.DifficultyType): List<GameQuestion<String>> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)

        val movies = gameRepository.getRandomMoviesWithPoster(gameDifficulty.totalQuestions * 4)

        return movies.chunked(4).map { group ->
            val correctMovie = group.random()
            val wrongChoices = group.filter { it != correctMovie }.map { it.name }

            val movieNameChoices = (wrongChoices + correctMovie.name).shuffled()

            GameQuestion(
                question = correctMovie.posterUrl,
                choices = movieNameChoices,
                correctChoice = correctMovie.name,
                questionTime = gameDifficulty.timeLimitSeconds
            )
        }
    }
}
