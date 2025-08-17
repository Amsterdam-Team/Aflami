package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class EvaluateWinConditionUseCaseTest {

    private val gameRepository: GameRepository = mockk()
    private val evaluateWinConditionUseCase = EvaluateWinConditionUseCase(gameRepository)

    @Test
    fun `should call getCollectedPoints when collected evaluateWinConditionUseCase called`() {
        every { gameRepository.getCollectedPoints(any()) } returns 0

        evaluateWinConditionUseCase(1L)

        verify(exactly = 1) { gameRepository.getCollectedPoints(any()) }
    }

    @Test
    fun `evaluateWinConditionUseCase should return true when collected points greater than 0`() {
        every { gameRepository.getCollectedPoints(1L) } returns 10

        val result = evaluateWinConditionUseCase(1L)

        assertThat(result).isTrue()
    }

    @Test
    fun `evaluateWinConditionUseCase should return false when collected points equal to 0`() {
        every { gameRepository.getCollectedPoints(1L) } returns 0

        val result = evaluateWinConditionUseCase(1L)

        assertThat(result).isFalse()
    }

    @Test
    fun `evaluateWinConditionUseCase should return false when collected points less than 0`() {
        every { gameRepository.getCollectedPoints(1L) } returns -5

        val result = evaluateWinConditionUseCase(1L)

        assertThat(result).isFalse()
    }
}