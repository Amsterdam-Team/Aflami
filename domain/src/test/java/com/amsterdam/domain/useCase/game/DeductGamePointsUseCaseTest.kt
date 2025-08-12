package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.repository.GamePointsRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeductGamePointsUseCaseTest {

    private val gamePointsRepository = mockk<GamePointsRepository>()
    private val deductGamePointsUseCase = DeductGamePointsUseCase(gamePointsRepository)

    @Test
    fun `should call updatePoints from repository when try deduct points`() = runTest {
        coEvery { gamePointsRepository.getPoints() } returns flowOf(20)
        coEvery { gamePointsRepository.updatePoints(any()) } just runs

        deductGamePointsUseCase(10)

        coVerify { gamePointsRepository.updatePoints(any()) }
    }

    @Test
    fun `should call getPoints from repository when try to deduct points`() = runTest {
        coEvery { gamePointsRepository.getPoints() } returns flowOf(20)
        coEvery { gamePointsRepository.updatePoints(any()) } just runs

        deductGamePointsUseCase(10)

        coVerify { gamePointsRepository.getPoints() }
    }

    @Test
    fun `deduct points should deduct points from current points if points are enough`() = runTest {
        coEvery { gamePointsRepository.updatePoints(any()) } just runs
        coEvery { gamePointsRepository.getPoints() } returns flowOf(10)

        deductGamePointsUseCase(10)

        assertThat(gamePointsRepository.getPoints().first()).isEqualTo(10)
    }

    @Test
    fun `deduct points should throw NotEnoughPointsException when points are not enough`() = runTest {
        coEvery { gamePointsRepository.updatePoints(any()) } just runs
        coEvery { gamePointsRepository.getPoints() } throws NotEnoughPointsException()

        assertThrows<NotEnoughPointsException> { deductGamePointsUseCase(10) }
    }
}