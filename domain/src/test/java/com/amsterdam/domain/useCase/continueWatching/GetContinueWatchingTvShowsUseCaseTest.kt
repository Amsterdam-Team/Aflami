package com.amsterdam.domain.useCase.continueWatching

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.amsterdam.domain.utils.TvShowWatchHistory
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetContinueWatchingTvShowsUseCaseTest {

    private val watchHistoryRepository: WatchHistoryRepository = mockk(relaxed = true)
    private val getContinueWatchingTvShowsUseCase by lazy {
        GetContinueWatchingTvShowsUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call getContinueWatchingTvShows with correct pagination parameters`() = runTest {

        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                page,
                pageSize
            )
        } returns flow { emit(emptyList()) }

        getContinueWatchingTvShowsUseCase(page, pageSize)

        coVerify(exactly = 1) { watchHistoryRepository.getContinueWatchingTvShows(page, pageSize) }
    }

    @Test
    fun `should return flow of tv show watch history when repository returns data`() = runTest {
        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                any(),
                any()
            )
        } returns flow { emit(fakeTvShowWatchHistory) }

        val result = getContinueWatchingTvShowsUseCase(1, 20)

        assertThat(result.first()).isEqualTo(fakeTvShowWatchHistory)
    }

    @Test
    fun `should return an empty flow when repository returns no data`() = runTest {
        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                any(),
                any()
            )
        } returns flow { emit(emptyList()) }

        val result = getContinueWatchingTvShowsUseCase(1, 20)

        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository throws an exception`() = runTest {
        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                any(),
                any()
            )
        } throws AflamiException()

        assertThrows<AflamiException> {
            getContinueWatchingTvShowsUseCase(1, 20).first()
        }
    }


    private val page = 2
    private val pageSize = 10
    private val fakeTvShowWatchHistory = listOf(
        TvShowWatchHistory(
            tvShow = fakeTvShowList.first(),
            lastWatchedTime = Instant.DISTANT_PAST
        )
    )
}