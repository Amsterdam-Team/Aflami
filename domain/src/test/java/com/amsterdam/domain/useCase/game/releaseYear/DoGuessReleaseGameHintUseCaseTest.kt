package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DoGuessReleaseGameHintUseCaseTest {
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase = mockk()
    private val updateUserPointsUseCase: UpdateUserGamePointsUseCase = mockk(relaxed = true)
    private val doGuessReleaseGameHintUseCase by lazy {
        DoGuessReleaseGameHintUseCase(getTotalUserPointsUseCase, updateUserPointsUseCase)
    }

    @Test
    fun `should remove one wrong release year and deduct points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(currentPoints)
        coEvery { updateUserPointsUseCase(any()) } just Runs

        val result = doGuessReleaseGameHintUseCase(movieReleasedDateQuestion)

        assertThat(result.releaseYearChoices.size).isEqualTo(movieReleasedDateQuestion.releaseYearChoices.size - 1)
    }

    @Test
    fun `should throw NotEnoughPointsException when not enough points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(lowPoints)

        assertThrows<NotEnoughPointsException> {
            doGuessReleaseGameHintUseCase(movieReleasedDateQuestion)
        }
    }

    private val currentPoints = 20
    private val lowPoints = 5
    private val movieReleasedDateQuestion = GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion(
        question = "Sample Movie",
        releaseYearChoices = listOf(1972, 1973, 1974),
        correctChoice = 1972,
        questionTimeSeconds = 30
    )
}