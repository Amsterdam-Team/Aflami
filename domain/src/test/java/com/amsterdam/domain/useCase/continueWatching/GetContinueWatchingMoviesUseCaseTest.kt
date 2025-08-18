package com.amsterdam.domain.useCase.continueWatching

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.domain.utils.MovieWatchHistory
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

class GetContinueWatchingMoviesUseCaseTest {

    private val watchHistoryRepository: WatchHistoryRepository = mockk(relaxed = true)
    private val getContinueWatchingMoviesUseCase by lazy {
        GetContinueWatchingMoviesUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call getContinueWatchingMovies with correct pagination parameters`() = runTest {
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                page,
                pageSize
            )
        } returns flow { emit(emptyList()) }

        getContinueWatchingMoviesUseCase(page, pageSize)

        coVerify(exactly = 1) { watchHistoryRepository.getContinueWatchingMovies(page, pageSize) }
    }

    @Test
    fun `should return flow of movie watch history when repository returns data`() = runTest {
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                any(),
                any()
            )
        } returns flow { emit(fakeMovieWatchHistory) }

        val result = getContinueWatchingMoviesUseCase(1, 20)

        assertThat(result.first()).isEqualTo(fakeMovieWatchHistory)
    }

    @Test
    fun `should return an empty flow when repository returns no data`() = runTest {
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                any(),
                any()
            )
        } returns flow { emit(emptyList()) }

        val result = getContinueWatchingMoviesUseCase(1, 20)

        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository throws an exception`() = runTest {
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                any(),
                any()
            )
        } throws AflamiException()

        assertThrows<AflamiException> {
            getContinueWatchingMoviesUseCase(1, 20).first()
        }
    }

    private val fakeMovieWatchHistory = listOf(
        MovieWatchHistory(
            movie = fakeMovieList.first(),
            lastWatchedTime = Instant.DISTANT_PAST
        )
    )

    private val page = 2
    private val pageSize = 10
}