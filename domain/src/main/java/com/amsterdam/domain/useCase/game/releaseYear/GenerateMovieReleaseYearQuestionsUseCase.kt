package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty.DifficultyType

class GenerateMovieReleaseYearQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<GameQuestion<Int>> {
      val gameDifficulty =  getGameDifficultyByDifficultyTypeUseCase(difficultyType)
        val movies = gameRepository.getRandomMoviesWithReleaseDate(gameDifficulty.totalQuestions)

        return movies.map { movie ->
            val correctYear = movie.releaseDate!!.year
            val choices = generateYearChoices(correctYear)
            GameQuestion(
                question = movie.name,
                choices = choices,
                correctChoice = correctYear,
                questionTime = gameDifficulty.timeLimitSeconds
            )
        }
    }

    private fun generateYearChoices(correctYear: Int, numberOfChoices: Int = 4): List<Int> {
        val choices = mutableSetOf(correctYear)

        while (choices.size < numberOfChoices) {
            val randomYear = (correctYear - 10..correctYear + 10).random()
            choices.add(randomYear)
        }

        return choices.shuffled()
    }
}