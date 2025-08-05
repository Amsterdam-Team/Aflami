package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.MovieWatchHistory
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetContinueWatchingMoviesUseCaseTest {

    private lateinit var watchHistoryRepository: WatchHistoryRepository
    private lateinit var getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase

    private val fakeMovie = Movie(
        id = 1L,
        name = "Test Movie",
        description = "",
        posterUrl = "",
        releaseDate = LocalDate(2023, 1, 1),
        categories = emptyList(),
        rating = 8.0f,
        popularity = 100.0,
        originCountry = "US",
        runTimeInMinutes = 120,
    )

    private val fakeMovieWatchHistory = listOf(
        MovieWatchHistory(
            movie = fakeMovie,
            lastWatchedTime = Instant.DISTANT_PAST
        )
    )

    @BeforeEach
    fun setUp() {
        watchHistoryRepository = mockk(relaxed = true)
        getContinueWatchingMoviesUseCase = GetContinueWatchingMoviesUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call getContinueWatchingMovies with correct pagination parameters`() = runTest {
        // Given
        val page = 2
        val pageSize = 10
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                page,
                pageSize
            )
        } returns flow { emit(emptyList()) }

        // When
        getContinueWatchingMoviesUseCase(page, pageSize)

        // Then
        coVerify(exactly = 1) { watchHistoryRepository.getContinueWatchingMovies(page, pageSize) }
    }

    @Test
    fun `should return flow of movie watch history when repository returns data`() = runTest {
        // Given
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                any(),
                any()
            )
        } returns flow { emit(fakeMovieWatchHistory) }

        // When
        val result = getContinueWatchingMoviesUseCase(1, 20)

        // Then
        assertThat(result.first()).isEqualTo(fakeMovieWatchHistory)
    }

    @Test
    fun `should return an empty flow when repository returns no data`() = runTest {
        // Given
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                any(),
                any()
            )
        } returns flow { emit(emptyList()) }

        // When
        val result = getContinueWatchingMoviesUseCase(1, 20)

        // Then
        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository throws an exception`() = runTest {
        // Given
        coEvery {
            watchHistoryRepository.getContinueWatchingMovies(
                any(),
                any()
            )
        } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            getContinueWatchingMoviesUseCase(1, 20).first()
        }
    }
}