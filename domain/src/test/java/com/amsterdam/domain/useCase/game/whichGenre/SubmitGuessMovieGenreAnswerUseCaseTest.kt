package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SubmitGuessMovieGenreAnswerUseCaseTest {
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase = mockk()
    private val updateUserGamePointsUseCase: UpdateUserGamePointsUseCase = mockk(relaxed = true)
    private val submitGuessMovieGenreAnswerUseCase by lazy {
        SubmitGuessMovieGenreAnswerUseCase(getGameDifficultyUseCase, updateUserGamePointsUseCase)
    }

    @Test
    fun `should return correct answer result and award points when called with correct answer`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty
        coEvery { updateUserGamePointsUseCase(gameDifficulty.pointsPerQuestion) } just Runs

        val result = submitGuessMovieGenreAnswerUseCase(movieGenreQuestion, MovieGenre.ACTION, difficultyType)

        assertThat(result).isEqualTo(expectedCorrectResult)
    }

    @Test
    fun `should return incorrect answer result with zero points when called with correct answer`() = runTest {
        every { getGameDifficultyUseCase(difficultyType) } returns gameDifficulty

        val result = submitGuessMovieGenreAnswerUseCase(movieGenreQuestion, MovieGenre.DRAMA, difficultyType)

        assertThat(result).isEqualTo(expectedIncorrectResult)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val gameDifficulty = GameDifficulty(5, 30, 10, GameDifficulty.DifficultyType.EASY)
    private val movieGenreQuestion = GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion(
        id = 1,
        question = "Test Movie",
        genreChoices = listOf(MovieGenre.ACTION, MovieGenre.DRAMA),
        correctChoice = MovieGenre.ACTION,
        questionTime = 30
    )
    private val expectedCorrectResult = SubmitGuessMovieGenreAnswerUseCase.AnswerResult(true, 10)
    private val expectedIncorrectResult = SubmitGuessMovieGenreAnswerUseCase.AnswerResult(false, 0)
}