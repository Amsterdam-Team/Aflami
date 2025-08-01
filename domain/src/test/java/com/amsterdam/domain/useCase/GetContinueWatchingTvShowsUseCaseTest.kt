package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.useCase.home.GetContinueWatchingTvShowsUseCase
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.TvShowWatchHistory
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

class GetContinueWatchingTvShowsUseCaseTest {

    private lateinit var watchHistoryRepository: WatchHistoryRepository
    private lateinit var getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase

    private val fakeTvShow = TvShow(
        id = 1L,
        name = "Test TvShow",
        description = "",
        posterUrl = "",
        airDate = LocalDate(2023, 1, 1),
        categories = emptyList(),
        rating = 8.0f,
        popularity = 100.0,
        seasonCount = 1,
        originCountry = "US",
        productionCompanies = emptyList()
    )

    private val fakeTvShowWatchHistory = listOf(
        TvShowWatchHistory(
            tvShow = fakeTvShow,
            lastWatchedTime = Instant.DISTANT_PAST
        )
    )

    @BeforeEach
    fun setUp() {
        watchHistoryRepository = mockk(relaxed = true)
        getContinueWatchingTvShowsUseCase =
            GetContinueWatchingTvShowsUseCase(watchHistoryRepository)
    }

    @Test
    fun `should call getContinueWatchingTvShows with correct pagination parameters`() = runTest {
        // Given
        val page = 2
        val pageSize = 10
        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                page,
                pageSize
            )
        } returns flow { emit(emptyList()) }

        // When
        getContinueWatchingTvShowsUseCase(page, pageSize)

        // Then
        coVerify(exactly = 1) { watchHistoryRepository.getContinueWatchingTvShows(page, pageSize) }
    }

    @Test
    fun `should return flow of tv show watch history when repository returns data`() = runTest {
        // Given
        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                any(),
                any()
            )
        } returns flow { emit(fakeTvShowWatchHistory) }

        // When
        val result = getContinueWatchingTvShowsUseCase(1, 20)

        // Then
        assertThat(result.first()).isEqualTo(fakeTvShowWatchHistory)
    }

    @Test
    fun `should return an empty flow when repository returns no data`() = runTest {
        // Given
        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                any(),
                any()
            )
        } returns flow { emit(emptyList()) }

        // When
        val result = getContinueWatchingTvShowsUseCase(1, 20)

        // Then
        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository throws an exception`() = runTest {
        // Given
        coEvery {
            watchHistoryRepository.getContinueWatchingTvShows(
                any(),
                any()
            )
        } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            getContinueWatchingTvShowsUseCase(1, 20).first()
        }
    }
}