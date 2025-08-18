package com.amsterdam.domain.useCase.continueWatching

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.amsterdam.domain.utils.MovieWatchHistory
import com.amsterdam.domain.utils.TvShowWatchHistory
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetContinueWatchingDataUseCaseTest {
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase = mockk()
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase = mockk()
    private val getContinueWatchingScreenDataUseCase by lazy {
        GetContinueWatchingDataUseCase(
            getContinueWatchingMoviesUseCase,
            getContinueWatchingTvShowsUseCase
        )
    }

    @Test
    fun `should call getContinueWatchingMoviesUseCases with half the page size`() = runTest {
        coEvery { getContinueWatchingMoviesUseCase(page, any()) } returns flowOf(emptyList())
        coEvery { getContinueWatchingTvShowsUseCase(page, any()) } returns flowOf(emptyList())

        getContinueWatchingScreenDataUseCase(page, pageSize)

        coVerify(exactly = 1) { getContinueWatchingMoviesUseCase(page, pageSize / 2) }
    }

    @Test
    fun `should call getContinueWatchingTvShowsUseCase with half the page size`() = runTest {
        coEvery { getContinueWatchingMoviesUseCase(page, any()) } returns flowOf(emptyList())
        coEvery { getContinueWatchingTvShowsUseCase(page, any()) } returns flowOf(emptyList())

        getContinueWatchingScreenDataUseCase(page, pageSize)

        coVerify(exactly = 1) { getContinueWatchingTvShowsUseCase(page, pageSize / 2) }
    }

    @Test
    fun `should return ContinueWatchingScreenData object with correct flows`() = runTest {
        val expectedMovieList = listOf(fakeMovieWatchHistory)
        val expectedTvShowList = listOf(fakeTvShowWatchHistory)

        val continueWatchingDataExpectedResult = GetContinueWatchingDataUseCase.ContinueWatchingData(
            expectedMovieList,
            expectedTvShowList
        )

        val movieFlow = flow { emit(expectedMovieList) }
        val tvShowFlow = flow { emit(expectedTvShowList) }

        coEvery { getContinueWatchingMoviesUseCase(any(), any()) } returns movieFlow
        coEvery { getContinueWatchingTvShowsUseCase(any(), any()) } returns tvShowFlow

        val result = getContinueWatchingScreenDataUseCase().first()

        assertThat(result).isEqualTo(continueWatchingDataExpectedResult)
    }


    @Test
    fun `should propagate exception from getContinueWatchingMoviesUseCase`() = runTest {
        coEvery { getContinueWatchingMoviesUseCase(any(), any()) } throws AflamiException()

        assertThrows<AflamiException> {
            getContinueWatchingScreenDataUseCase().single()
        }
    }

    @Test
    fun `should propagate exception from getContinueWatchingTvShowsUseCase`() = runTest {
        coEvery {
            getContinueWatchingMoviesUseCase(
                any(),
                any()
            )
        } returns flow { emit(emptyList()) }
        coEvery { getContinueWatchingTvShowsUseCase(any(), any()) } throws AflamiException()

        assertThrows<AflamiException> {
            getContinueWatchingScreenDataUseCase().single()
        }
    }

    private val page = 1
    private val pageSize = 20

    private val fakeMovieWatchHistory = MovieWatchHistory(
        movie = fakeMovieList.first(),
        lastWatchedTime = Instant.DISTANT_PAST
    )
    private val fakeTvShowWatchHistory = TvShowWatchHistory(
        tvShow = fakeTvShowList.first(),
        lastWatchedTime = Instant.DISTANT_PAST
    )
}