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

class AddMovieWatchHistoryUseCaseTest {
    private lateinit var watchHistoryRepository: WatchHistoryRepository
    private lateinit var addMovieWatchHistoryUseCase: AddMovieWatchHistoryUseCase

    @BeforeEach
    fun setUp() {
        watchHistoryRepository = mockk(relaxed = true)
        addMovieWatchHistoryUseCase = AddMovieWatchHistoryUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call addMovieToWatchHistory with the correct movieId`() = runTest {
        // Given
        val movieId = 123L
        coJustRun { watchHistoryRepository.addMovieToWatchHistory(movieId) }

        // When
        addMovieWatchHistoryUseCase(movieId)

        // Then
        coVerify(exactly = 1) { watchHistoryRepository.addMovieToWatchHistory(movieId) }
    }

    @Test
    fun `should propagate AflamiException when repository call fails`() = runTest {
        // Given
        val movieId = 123L
        coEvery { watchHistoryRepository.addMovieToWatchHistory(movieId) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            addMovieWatchHistoryUseCase(movieId)
        }
    }

    @Test
    fun `should handle a negative movieId gracefully`() = runTest {
        // Given
        val invalidMovieId = -1L
        coJustRun { watchHistoryRepository.addMovieToWatchHistory(invalidMovieId) }

        // When
        addMovieWatchHistoryUseCase(invalidMovieId)

        // Then
        coVerify(exactly = 1) { watchHistoryRepository.addMovieToWatchHistory(invalidMovieId) }
    }
}