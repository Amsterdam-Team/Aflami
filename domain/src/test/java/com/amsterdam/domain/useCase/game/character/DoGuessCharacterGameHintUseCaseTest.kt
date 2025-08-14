package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DoGuessCharacterGameHintUseCaseTest {
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase = mockk()
    private val updateUserPointsUseCase: UpdateUserGamePointsUseCase = mockk(relaxed = true)
    private val doGuessMovieByCharacterGameHintUseCase by lazy {
        DoGuessCharacterGameHintUseCase(getTotalUserPointsUseCase, updateUserPointsUseCase)
    }

    @Test
    fun `should not throw exception when user has enough points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(currentPoints)
        coEvery { updateUserPointsUseCase(any()) } just Runs

        assertDoesNotThrow {
            doGuessMovieByCharacterGameHintUseCase(movieCharacterQuestion)
        }
    }

    @Test
    fun `should throw NotEnoughPointsException when not enough points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(lowPoints)

        assertThrows<NotEnoughPointsException> {
            doGuessMovieByCharacterGameHintUseCase(movieCharacterQuestion)
        }
    }

    private val currentPoints = 20
    private val lowPoints = 5
    private val movieCharacterQuestion = GenerateCharacterQuestionsUseCase.CharacterDataQuestion(
        questionAsPosterUrl = "character",
        choices = listOf("john", "man", "woman"),
        correctAnswer = "john",
        questionTimeSeconds = 30
    )
}