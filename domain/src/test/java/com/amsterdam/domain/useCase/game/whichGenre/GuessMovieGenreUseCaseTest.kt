package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GuessMovieGenreUseCaseTest {
    private val generateMovieGenreQuestionsUseCase: GenerateMovieGenreQuestionsUseCase = mockk()
    private val submitGuessMovieGenreAnswerUseCase: SubmitGuessMovieGenreAnswerUseCase = mockk()
    private val doGuessGenreGameHintUseCase: DoGuessGenreGameHintUseCase = mockk()
    private val guessMovieGenreUseCase by lazy {
        GuessMovieGenreUseCase(
            generateMovieGenreQuestionsUseCase,
            submitGuessMovieGenreAnswerUseCase,
            doGuessGenreGameHintUseCase
        )
    }

    @Test
    fun `startGame should call generateMovieGenreQuestionsUseCase when invoked`() = runTest {
        coEvery { generateMovieGenreQuestionsUseCase(difficultyType) } returns listOf(testQuestion)

        val result = guessMovieGenreUseCase.startGame(difficultyType)

        assertThat(result).isEqualTo(listOf(testQuestion))
    }

    @Test
    fun `giveHint should call doGuessGenreGameHintUseCase`() = runTest {
        coEvery { doGuessGenreGameHintUseCase(testQuestion) } returns hintedQuestion

        val result = guessMovieGenreUseCase.giveHint(testQuestion)

        assertThat(result).isEqualTo(hintedQuestion)
    }

    @Test
    fun `checkAnswer should call submitGuessMovieGenreAnswerUseCase`() = runTest {
        coEvery { submitGuessMovieGenreAnswerUseCase(testQuestion, MovieGenre.ACTION, difficultyType) } returns answerResult

        val result = guessMovieGenreUseCase.checkAnswer(testQuestion, MovieGenre.ACTION, difficultyType)

        assertThat(result).isEqualTo(answerResult)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val testQuestion = GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion(
        id = 1,
        question = "Test Movie",
        genreChoices = listOf(MovieGenre.ACTION, MovieGenre.DRAMA),
        correctChoice = MovieGenre.ACTION,
        questionTime = 30
    )
    private val hintedQuestion = testQuestion.copy(genreChoices = listOf(MovieGenre.ACTION))
    private val answerResult = SubmitGuessMovieGenreAnswerUseCase.AnswerResult(true, 10)
}