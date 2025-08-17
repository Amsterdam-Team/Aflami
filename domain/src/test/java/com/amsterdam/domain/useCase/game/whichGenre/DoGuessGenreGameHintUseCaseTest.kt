package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DoGuessGenreGameHintUseCaseTest {
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase = mockk()
    private val updateUserPointsUseCase: UpdateUserGamePointsUseCase = mockk(relaxed = true)
    private val doGuessGenreGameHintUseCase by lazy {
        DoGuessGenreGameHintUseCase(getTotalUserPointsUseCase, updateUserPointsUseCase)
    }

    @Test
    fun `should remove one wrong genre and deduct points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(currentPoints)
        coEvery { updateUserPointsUseCase(any()) } just Runs

        val result = doGuessGenreGameHintUseCase(movieGenreQuestion)

        assertThat(result.choices.size).isEqualTo(movieGenreQuestion.choices.size - 1)
    }

    @Test
    fun `should throw NotEnoughPointsException when not enough points`() = runTest {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(lowPoints)

        assertThrows<NotEnoughPointsException> {
            doGuessGenreGameHintUseCase(movieGenreQuestion)
        }
    }

    private val currentPoints = 20
    private val lowPoints = 5
    private val movieGenreQuestion = GameQuestion(
        question = "Sample Movie",
        choices = listOf(MovieGenre.ACTION, MovieGenre.DRAMA, MovieGenre.COMEDY),
        correctChoice = MovieGenre.ACTION,
        questionTime = 30
    )
}
