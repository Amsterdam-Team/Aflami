package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingTvShowsUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.TvShowWatchHistory
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetContinueWatchingScreenDataUseCaseTest {
    private lateinit var getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase
    private lateinit var getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase
    private lateinit var getContinueWatchingScreenDataUseCase: GetContinueWatchingScreenDataUseCase

    @BeforeEach
    fun setUp() {
        getContinueWatchingMoviesUseCase = mockk()
        getContinueWatchingTvShowsUseCase = mockk()
        getContinueWatchingScreenDataUseCase =
            GetContinueWatchingScreenDataUseCase(
                getContinueWatchingMoviesUseCase,
                getContinueWatchingTvShowsUseCase
            )
    }

    @Test
    fun `should call both child use cases with half the page size`() = runTest {
        // Given
        val page = 1
        val pageSize = 20
        coEvery { getContinueWatchingMoviesUseCase(page, pageSize / 2) } returns flow {
            emit(
                emptyList()
            )
        }
        coEvery { getContinueWatchingTvShowsUseCase(page, pageSize / 2) } returns flow {
            emit(
                emptyList()
            )
        }

        // When
        getContinueWatchingScreenDataUseCase(page, pageSize)

        // Then
        coVerify(exactly = 1) { getContinueWatchingMoviesUseCase(page, 10) }
        coVerify(exactly = 1) { getContinueWatchingTvShowsUseCase(page, 10) }
    }

    @Test
    fun `should return ContinueWatchingScreenData object with correct flows`() = runTest {
        // Given
        val movieFlow = flow { emit(listOf(fakeMovieWatchHistory)) }
        val tvShowFlow = flow { emit(listOf(fakeTvShowWatchHistory)) }
        coEvery { getContinueWatchingMoviesUseCase(any(), any()) } returns movieFlow
        coEvery { getContinueWatchingTvShowsUseCase(any(), any()) } returns tvShowFlow

        // When
        val result = getContinueWatchingScreenDataUseCase()

        // Then
        assertThat(result.continueWatchingMovies.first()).isEqualTo(listOf(fakeMovieWatchHistory))
        assertThat(result.continueWatchingTvShows.first()).isEqualTo(listOf(fakeTvShowWatchHistory))
    }

    @Test
    fun `should propagate exception from getContinueWatchingMoviesUseCase`() = runTest {
        // Given
        coEvery { getContinueWatchingMoviesUseCase(any(), any()) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            getContinueWatchingScreenDataUseCase().continueWatchingMovies.single()
        }
    }

    @Test
    fun `should propagate exception from getContinueWatchingTvShowsUseCase`() = runTest {
        // Given
        coEvery {
            getContinueWatchingMoviesUseCase(
                any(),
                any()
            )
        } returns flow { emit(emptyList()) }
        coEvery { getContinueWatchingTvShowsUseCase(any(), any()) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            getContinueWatchingScreenDataUseCase().continueWatchingTvShows.single()
        }
    }

    private companion object {
        val fakeMovieWatchHistory = MovieWatchHistory(
            movie = Movie(
                id = 1L, name = "Movie", description = "", posterUrl = "",
                releaseDate = LocalDate(2023, 1, 1), categories = emptyList(), rating = 1.0f,
                popularity = 1.0, originCountry = "", runTimeInMinutes = 1, hasVideo = false,
                productionCompanies = emptyList()
            ),
            lastWatchedTime = Instant.DISTANT_PAST
        )
        val fakeTvShowWatchHistory = TvShowWatchHistory(
            tvShow = TvShow(
                id = 1L,
                name = "TV Show",
                description = "",
                posterUrl = "",
                airDate = LocalDate(2023, 1, 1),
                categories = emptyList(),
                rating = 1.0f,
                popularity = 1.0,
                seasonCount = 1,
                originCountry = "",
                productionCompanies = emptyList()
            ),
            lastWatchedTime = Instant.DISTANT_PAST
        )
    }
}