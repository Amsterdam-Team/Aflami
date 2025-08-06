package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WatchHistoryRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddTvShowWatchHistoryUseCaseTest {
    private lateinit var watchHistoryRepository: WatchHistoryRepository
    private lateinit var addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase

    @BeforeEach
    fun setUp() {
        watchHistoryRepository = mockk(relaxed = true)
        addTvShowWatchHistoryUseCase = AddTvShowWatchHistoryUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call addTvShowToWatchHistory with the correct tvShowId`() = runTest {
        // Given
        val tvShowId = 123L
        coJustRun { watchHistoryRepository.addTvShowToWatchHistory(tvShowId) }

        // When
        addTvShowWatchHistoryUseCase(tvShowId)

        // Then
        coVerify(exactly = 1) { watchHistoryRepository.addTvShowToWatchHistory(tvShowId) }
    }

    @Test
    fun `should propagate AflamiException when repository call fails`() = runTest {
        // Given
        val tvShowId = 123L
        coEvery { watchHistoryRepository.addTvShowToWatchHistory(tvShowId) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            addTvShowWatchHistoryUseCase(tvShowId)
        }
    }

    @Test
    fun `should handle a negative tvShowId gracefully`() = runTest {
        // Given
        val invalidTvShowId = -1L
        coJustRun { watchHistoryRepository.addTvShowToWatchHistory(invalidTvShowId) }

        // When
        addTvShowWatchHistoryUseCase(invalidTvShowId)

        // Then
        coVerify(exactly = 1) { watchHistoryRepository.addTvShowToWatchHistory(invalidTvShowId) }
    }
}