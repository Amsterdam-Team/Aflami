package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.repository.GameRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class UpdateUserGamePointsUseCaseTest {
    private val gameRepository: GameRepository = mockk(relaxed = true)
    private val updateUserGamePointsUseCase by lazy { UpdateUserGamePointsUseCase(gameRepository) }

    @Test
    fun `should update user points when called with valid points`() = runTest {
        every { gameRepository.getUserPoints() } returns flowOf(currentPoints)

        updateUserGamePointsUseCase(pointsToAdd)

        coVerify(exactly = 1) { gameRepository.updatePoints(expectedUpdatedPoints) }
    }

    private val currentPoints = 100
    private val pointsToAdd = 50
    private val expectedUpdatedPoints = 150
}