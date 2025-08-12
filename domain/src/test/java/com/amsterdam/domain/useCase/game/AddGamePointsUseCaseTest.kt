package com.amsterdam.domain.useCase.game

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

class AddGamePointsUseCaseTest {

    private val gamePointsRepository = mockk<GamePointsRepository>()
    private val addGamePointsUseCase = AddGamePointsUseCase(gamePointsRepository)

    @Test
    fun `should call updatePoints from repository when try add points`() = runTest {
        coEvery { gamePointsRepository.getPoints() } returns flowOf(0)
        coEvery { gamePointsRepository.updatePoints(any()) } just runs

        addGamePointsUseCase(10)

        coVerify { gamePointsRepository.updatePoints(any()) }
    }

    @Test
    fun `should call getPoints from repository when try to add points`() = runTest {
        coEvery { gamePointsRepository.getPoints() } returns flowOf(0)
        coEvery { gamePointsRepository.updatePoints(any()) } just runs

        addGamePointsUseCase(10)

        coVerify { gamePointsRepository.getPoints() }
    }

    @Test
    fun `add points use case should add points to current points`() = runTest {
        coEvery { gamePointsRepository.updatePoints(any()) } just runs
        coEvery { gamePointsRepository.getPoints() } returns flowOf(10)

        addGamePointsUseCase(10)

        assertThat(gamePointsRepository.getPoints().first()).isEqualTo(10)
    }
}