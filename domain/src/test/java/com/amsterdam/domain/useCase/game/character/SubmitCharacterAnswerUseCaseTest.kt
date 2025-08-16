package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SubmitCharacterAnswerUseCaseTest {
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase = mockk()
    private val updateUserGamePointsUseCase: UpdateUserGamePointsUseCase = mockk(relaxed = true)
    private val submitCharacterAnswerUseCase by lazy {
        SubmitCharacterAnswerUseCase(getGameDifficultyUseCase, updateUserGamePointsUseCase)
    }

    @Test
    fun `should return correct answer result and award points`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty
        coEvery { updateUserGamePointsUseCase(gameDifficulty.pointsPerQuestion) } just Runs

        val result = submitCharacterAnswerUseCase(movieCharacterQuestion, "john", difficultyType)

        assertThat(result).isEqualTo(expectedCorrectResult)
    }

    @Test
    fun `should return incorrect answer result with zero points`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty

        val result = submitCharacterAnswerUseCase(movieCharacterQuestion, "man", difficultyType)

        assertThat(result).isEqualTo(expectedIncorrectResult)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val gameDifficulty = GameDifficulty(5, 30, 10, GameDifficulty.DifficultyType.EASY)
    private val movieCharacterQuestion = GameQuestion(
        question = "character",
        choices = listOf("john", "man", "woman"),
        correctChoice = "john",
        questionTime = 30
    )
    private val expectedCorrectResult = AnswerResult(true, 10)
    private val expectedIncorrectResult = AnswerResult(false, 0)
}