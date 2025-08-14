package com.amsterdam.domain.useCase.game

import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GetGameDifficultyByDifficultyTypeUseCaseTest {
    private val getGameDifficultyByDifficultyTypeUseCase by lazy { GetGameDifficultyByDifficultyTypeUseCase() }

    @Test
    fun `should return easy difficulty settings when called with easy option`() {
        val result = getGameDifficultyByDifficultyTypeUseCase(DifficultyType.EASY)

        assertThat(result).isEqualTo(expectedEasyDifficulty)
    }

    @Test
    fun `should return medium difficulty settings when called with medium option`() {
        val result = getGameDifficultyByDifficultyTypeUseCase(DifficultyType.MEDIUM)

        assertThat(result).isEqualTo(expectedMediumDifficulty)
    }

    @Test
    fun `should return hard difficulty settings when called with hard option`() {
        val result = getGameDifficultyByDifficultyTypeUseCase(DifficultyType.HARD)

        assertThat(result).isEqualTo(expectedHardDifficulty)
    }

    private val expectedEasyDifficulty = GameDifficulty(
        totalQuestions = 5,
        timeLimitSeconds = 45,
        pointsPerQuestion = 5,
        difficultyType = DifficultyType.EASY
    )
    private val expectedMediumDifficulty = GameDifficulty(
        totalQuestions = 10,
        timeLimitSeconds = 30,
        pointsPerQuestion = 10,
        difficultyType = DifficultyType.MEDIUM
    )
    private val expectedHardDifficulty = GameDifficulty(
        totalQuestions = 20,
        timeLimitSeconds = 10,
        pointsPerQuestion = 20,
        difficultyType = DifficultyType.HARD
    )
}