package com.amsterdam.domain.useCase.game.whichGenre

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

class GenerateMovieGenreQuestionsUseCaseTest {
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase = mockk()
    private val gameRepository: GameRepository = mockk()
    private val generateMovieGenreQuestionsUseCase by lazy {
        GenerateMovieGenreQuestionsUseCase(getGameDifficultyUseCase, gameRepository)
    }

    @Test
    fun `should generate movie genre questions`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty
        coEvery { gameRepository.getRandomMoviesWithReleaseDate(gameDifficulty.totalQuestions) } returns generateFakeMoviesByCount(gameDifficulty.totalQuestions)

        val result = generateMovieGenreQuestionsUseCase(difficultyType)

        assertThat(result.size).isEqualTo(gameDifficulty.totalQuestions)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val gameDifficulty = GameDifficulty(5, 30, 10, GameDifficulty.DifficultyType.EASY)
}