package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WatchHistoryRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddMovieWatchHistoryUseCaseTest {
    private val watchHistoryRepository: WatchHistoryRepository = mockk(relaxed = true)
    private val addMovieWatchHistoryUseCase by lazy {
        AddMovieWatchHistoryUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call addMovieToWatchHistory when movieId is valid`() = runTest {
        coJustRun { watchHistoryRepository.addMovieToWatchHistory(movieId) }

        addMovieWatchHistoryUseCase(movieId)

        coVerify(exactly = 1) { watchHistoryRepository.addMovieToWatchHistory(movieId) }
    }

    @Test
    fun `should propagate AflamiException when repository call fails`() = runTest {
        coEvery { watchHistoryRepository.addMovieToWatchHistory(movieId) } throws AflamiException()

        assertThrows<AflamiException> {
            addMovieWatchHistoryUseCase(movieId)
        }
    }

    @Test
    fun `should fail gracefully when given invalidMovieId`() = runTest {
        coJustRun { watchHistoryRepository.addMovieToWatchHistory(invalidMovieId) }

        addMovieWatchHistoryUseCase(invalidMovieId)

        coVerify(exactly = 1) { watchHistoryRepository.addMovieToWatchHistory(invalidMovieId) }
    }

    private val movieId = 123L
    private val invalidMovieId = -1L
}