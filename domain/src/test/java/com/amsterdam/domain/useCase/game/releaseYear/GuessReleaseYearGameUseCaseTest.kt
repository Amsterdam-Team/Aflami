package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GuessReleaseYearGameUseCaseTest {
    private val generateMovieReleaseYearQuestionsUseCase: GenerateMovieReleaseYearQuestionsUseCase = mockk()
    private val submitGuessReleaseYearAnswerUseCase: SubmitGuessReleaseYearAnswerUseCase = mockk()
    private val doGuessReleaseGameHintUseCase: DoGuessReleaseGameHintUseCase = mockk()
    private val guessReleaseYearGameUseCase by lazy {
        GuessReleaseYearGameUseCase(
            generateMovieReleaseYearQuestionsUseCase,
            doGuessReleaseGameHintUseCase,
            submitGuessReleaseYearAnswerUseCase
        )
    }

    @Test
    fun `startGame should call generateMovieReleaseYearQuestionsUseCase`() = runTest {
        coEvery { generateMovieReleaseYearQuestionsUseCase(difficultyType) } returns listOf(movieReleasedDateQuestion)

        val result = guessReleaseYearGameUseCase.startGame(difficultyType)

        assertThat(result).isEqualTo(listOf<GameQuestion<Int>>(movieReleasedDateQuestion))
    }

    @Test
    fun `giveHint should call doGuessReleaseGameHintUseCase`() = runTest {
        coEvery { doGuessReleaseGameHintUseCase(movieReleasedDateQuestion) } returns hintedQuestion

        val result = guessReleaseYearGameUseCase.giveHint(movieReleasedDateQuestion)

        assertThat(result).isEqualTo(hintedQuestion)
    }

    @Test
    fun `checkAnswer should call submitGuessReleaseYearAnswerUseCase`() = runTest {
        coEvery { submitGuessReleaseYearAnswerUseCase(movieReleasedDateQuestion, 1972, difficultyType) } returns answerResult

        val result = guessReleaseYearGameUseCase.answer(movieReleasedDateQuestion, 1972, difficultyType)

        assertThat(result).isEqualTo(answerResult)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val movieReleasedDateQuestion = GameQuestion(
        question = "Sample Movie",
        choices = listOf(1972, 1973, 1974),
        correctChoice = 1972,
        questionTime = 30
    )
    private val hintedQuestion = movieReleasedDateQuestion.copy(choices = listOf(1972))
    private val answerResult = AnswerResult(true, 10)
}