package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SubmitGuessReleaseYearAnswerUseCaseTest {
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase = mockk()
    private val updateUserGamePointsUseCase: UpdateUserGamePointsUseCase = mockk(relaxed = true)
    private val submitGuessReleaseYearAnswerUseCase by lazy {
        SubmitGuessReleaseYearAnswerUseCase(getGameDifficultyUseCase, updateUserGamePointsUseCase)
    }

    @Test
    fun `should return correct answer result and award points`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty
        coEvery { updateUserGamePointsUseCase(gameDifficulty.pointsPerQuestion) } just Runs

        val result = submitGuessReleaseYearAnswerUseCase(movieReleasedDateQuestion, 1972, difficultyType)

        assertThat(result).isEqualTo(expectedCorrectResult)
    }

    @Test
    fun `should return incorrect answer result with zero points`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty

        val result = submitGuessReleaseYearAnswerUseCase(movieReleasedDateQuestion, 1973, difficultyType)

        assertThat(result).isEqualTo(expectedIncorrectResult)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val gameDifficulty = GameDifficulty(5, 30, 10, GameDifficulty.DifficultyType.EASY)

    private val movieReleasedDateQuestion = GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion(
        question = "Sample Movie",
        releaseYearChoices = listOf(1972, 1973, 1974),
        correctChoice = 1972,
        questionTimeSeconds = 30
    )

    private val expectedCorrectResult = SubmitGuessReleaseYearAnswerUseCase.AnswerResult(true, 10)
    private val expectedIncorrectResult = SubmitGuessReleaseYearAnswerUseCase.AnswerResult(false, 0)
}