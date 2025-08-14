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

class AddTvShowWatchHistoryUseCaseTest {
    private val watchHistoryRepository: WatchHistoryRepository = mockk(relaxed = true)
    private val addTvShowWatchHistoryUseCase by lazy {
        AddTvShowWatchHistoryUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call addTvShowToWatchHistory when tvShowId is valid`() = runTest {
        coJustRun { watchHistoryRepository.addTvShowToWatchHistory(tvShowId) }

        addTvShowWatchHistoryUseCase(tvShowId)

        coVerify(exactly = 1) { watchHistoryRepository.addTvShowToWatchHistory(tvShowId) }
    }

    @Test
    fun `should propagate AflamiException when repository call fails`() = runTest {
        coEvery { watchHistoryRepository.addTvShowToWatchHistory(tvShowId) } throws AflamiException()

        assertThrows<AflamiException> {
            addTvShowWatchHistoryUseCase(tvShowId)
        }
    }

    @Test
    fun `should fail gracefully when given tvShowId`() = runTest {
        coJustRun { watchHistoryRepository.addTvShowToWatchHistory(invalidTvShowId) }

        addTvShowWatchHistoryUseCase(invalidTvShowId)

        coVerify(exactly = 1) { watchHistoryRepository.addTvShowToWatchHistory(invalidTvShowId) }
    }

    private val tvShowId = 123L
    private val invalidTvShowId = -1L
}