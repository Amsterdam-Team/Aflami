package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.utils.generateFakeMoviesByCount
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GenerateMoviePosterQuestionsUseCaseTest {
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase = mockk()
    private val gameRepository: GameRepository = mockk(relaxed = true)
    private val generateMoviePosterQuestionsUseCase by lazy {
        GenerateMoviePosterQuestionsUseCase(gameRepository, getGameDifficultyUseCase)
    }

    @Test
    fun `should generate movie poster questions`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty
        coEvery { gameRepository.getRandomMoviesWithPoster(gameDifficulty.totalQuestions * 4) } returns generateFakeMoviesByCount(gameDifficulty.totalQuestions * 4)

        val result = generateMoviePosterQuestionsUseCase(difficultyType)

        assertThat(result.size).isEqualTo(gameDifficulty.totalQuestions)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val gameDifficulty = GameDifficulty(5, 30, 10, GameDifficulty.DifficultyType.EASY)
}