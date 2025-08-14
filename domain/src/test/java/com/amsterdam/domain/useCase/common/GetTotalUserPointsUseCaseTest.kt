package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.repository.GameRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTotalUserPointsUseCaseTest {

    private val gamePointsRepository = mockk<GameRepository>()
    private val getTotalUserPointsUseCase = GetTotalUserPointsUseCase(gamePointsRepository)

    @Test
    fun `should call getPoints from repository`() = runTest {
        coEvery { gamePointsRepository.getUserPoints() } returns flowOf(40)

        getTotalUserPointsUseCase.invoke()

        coVerify(exactly = 1) { gamePointsRepository.getUserPoints() }
    }

    @Test
    fun `should return the same points flow as repository`() = runTest {
        every { gamePointsRepository.getUserPoints() } returns flowOf(40)

        assertThat(getTotalUserPointsUseCase().first()).isEqualTo(40)
    }
}