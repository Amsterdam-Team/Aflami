package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GuessMovieByPosterGameUseCaseTest {
    private val generateMoviePosterQuestionsUseCase: GenerateMoviePosterQuestionsUseCase = mockk()
    private val submitGuessMovieByPosterAnswerUseCase: SubmitGuessMovieByPosterAnswerUseCase = mockk()
    private val doGuessMovieByPosterHintUseCase: DoGuessMovieByPosterHintUseCase = mockk()
    private val guessMovieByPosterGameUseCase by lazy {
        GuessMovieByPosterGameUseCase(
            generateMoviePosterQuestionsUseCase,
            doGuessMovieByPosterHintUseCase,
            submitGuessMovieByPosterAnswerUseCase
        )
    }

    @Test
    fun `startGame should call generateMoviePosterQuestionsUseCase`() = runTest {
        coEvery { generateMoviePosterQuestionsUseCase(difficultyType) } returns listOf(moviePosterQuestion)

        val result = guessMovieByPosterGameUseCase.startGame(difficultyType)

        assertThat(result).isEqualTo(listOf<GameQuestion<String>>(moviePosterQuestion))
    }

    @Test
    fun `giveHint should call doGuessMovieByPosterHintUseCase`() = runTest {
        coEvery { doGuessMovieByPosterHintUseCase(moviePosterQuestion) } returns hintedQuestion

        val result = guessMovieByPosterGameUseCase.giveHint(moviePosterQuestion)

        assertThat(result).isEqualTo(hintedQuestion)
    }

    @Test
    fun `checkAnswer should call submitGuessMovieByPosterAnswerUseCase`() = runTest {
        coEvery { submitGuessMovieByPosterAnswerUseCase(moviePosterQuestion, "spiderman", difficultyType) } returns answerResult

        val result = guessMovieByPosterGameUseCase.answer(moviePosterQuestion, "spiderman", difficultyType)

        assertThat(result).isEqualTo(answerResult)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val moviePosterQuestion = GameQuestion(
        question = "Sample Movie",
        choices = listOf("spiderman", "batman", "superman"),
        correctChoice = "spiderman",
        questionTime = 30
    )
    private val hintedQuestion = moviePosterQuestion.copy(choices = listOf("spiderman"))
    private val answerResult = AnswerResult(true, 10)
}