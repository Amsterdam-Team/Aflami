package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GuessCharacterGameUseCaseTest {
    private val generateCharacterQuestionsUseCase: GenerateCharacterQuestionsUseCase = mockk()
    private val submitCharacterAnswerUseCase: SubmitCharacterAnswerUseCase = mockk()
    private val guessCharacterGameHintUseCase: DoGuessCharacterGameHintUseCase = mockk()
    private val guessCharacterGameUseCase by lazy {
        GuessCharacterGameUseCase(
            generateCharacterQuestionsUseCase,
            guessCharacterGameHintUseCase,
            submitCharacterAnswerUseCase
        )
    }

    @Test
    fun `startGame should call generateCharacterQuestionsUseCase`() = runTest {
        coEvery { generateCharacterQuestionsUseCase(difficultyType) } returns listOf(movieCharacterQuestion)

        val result = guessCharacterGameUseCase.startGame(difficultyType)

        assertThat(result).isEqualTo(listOf(movieCharacterQuestion))
    }

    @Test
    fun `giveHint should call guessCharacterGameHintUseCase`() = runTest {
        coEvery { guessCharacterGameHintUseCase(movieCharacterQuestion) } returns hintedQuestion

        val result = guessCharacterGameUseCase.giveHint(movieCharacterQuestion)

        assertThat(result).isEqualTo(hintedQuestion)
    }

    @Test
    fun `checkAnswer should call submitCharacterAnswerUseCase`() = runTest {
        coEvery { submitCharacterAnswerUseCase(movieCharacterQuestion, "john", difficultyType) } returns answerResult

        val result = guessCharacterGameUseCase.answer(movieCharacterQuestion, "john", difficultyType)

        assertThat(result).isEqualTo(answerResult)
    }

    private val difficultyType = GameDifficulty.DifficultyType.EASY
    private val movieCharacterQuestion = GameQuestion(
        question = "character",
        choices = listOf("john", "man", "woman"),
        correctChoice = "john",
        questionTime = 30
    )
    private val hintedQuestion = movieCharacterQuestion.copy(choices = listOf("john"))
    private val answerResult = AnswerResult(true, 10)
}