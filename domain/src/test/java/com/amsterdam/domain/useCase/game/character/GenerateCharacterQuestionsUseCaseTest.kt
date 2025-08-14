package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.utils.generateFakePeopleByCount
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GenerateCharacterQuestionsUseCaseTest {
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase = mockk()
    private val gameRepository: GameRepository = mockk(relaxed = true)
    private val generateMovieCharacterQuestionsUseCase by lazy {
        GenerateCharacterQuestionsUseCase(gameRepository, getGameDifficultyUseCase)
    }

    @Test
    fun `should generate movie character questions`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty
        coEvery { gameRepository.getCharacterDataQuestions(gameDifficulty.totalQuestions * 4) } returns generateFakePeopleByCount(gameDifficulty.totalQuestions * 4)

        val result = generateMovieCharacterQuestionsUseCase(difficultyType)

        assertThat(result.size).isEqualTo(gameDifficulty.totalQuestions)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val gameDifficulty = GameDifficulty(5, 30, 10, GameDifficulty.DifficultyType.EASY)
}