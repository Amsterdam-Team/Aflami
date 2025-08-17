package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.GameQuestion
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DoGuessMovieByPosterHintUseCaseTest {
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase = mockk()
    private val updateUserPointsUseCase: UpdateUserGamePointsUseCase = mockk(relaxed = true)
    private val doGuessMovieByPosterGameHintUseCase by lazy {
        DoGuessMovieByPosterHintUseCase(getTotalUserPointsUseCase, updateUserPointsUseCase)
    }

    @Test
    fun `should not throw exception when user has enough points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(currentPoints)
        coEvery { updateUserPointsUseCase(any()) } just Runs

        assertDoesNotThrow {
            doGuessMovieByPosterGameHintUseCase(moviePosterQuestion)
        }
    }

    @Test
    fun `should throw NotEnoughPointsException when not enough points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(lowPoints)

        assertThrows<NotEnoughPointsException> {
            doGuessMovieByPosterGameHintUseCase(moviePosterQuestion)
        }
    }

    private val currentPoints = 20
    private val lowPoints = 5
    private val moviePosterQuestion = GameQuestion(
        question = "Sample Movie",
        choices = listOf("spiderman", "batman", "superman"),
        correctChoice = "spiderman",
        questionTime = 30
    )
}