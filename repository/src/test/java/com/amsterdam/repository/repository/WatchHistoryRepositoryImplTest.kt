package com.amsterdam.repository.repository

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WatchHistoryRepositoryImplTest {

    private lateinit var repository: WatchHistoryRepositoryImpl

    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource = mockk()
    private val movieLocalDataSource: MovieLocalDataSource = mockk()
    private val movieRemoteDataSource: MovieRemoteDataSource = mockk()
    private val tvShowLocalDataSource: TvShowLocalDataSource = mockk()
    private val tvShowRemoteSource: TvShowsRemoteDataSource = mockk()
    private val preferences: AppPreferences = mockk()
    private val localTvDataSource: TvShowLocalDataSource = mockk()


    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = WatchHistoryRepositoryImpl(
            watchHistoryLocalDataSource = watchHistoryLocalDataSource,
            movieLocalDataSource = movieLocalDataSource,
            movieRemoteDataSource = movieRemoteDataSource,
            tvShowLocalDataSource = tvShowLocalDataSource,
            tvShowRemoteSource = tvShowRemoteSource,
            preferences = preferences,
            localTvDataSource = localTvDataSource
        )
        coEvery { preferences.getAppLanguage() } returns flowOf("en")

    }

    @Test
    fun `addMovieToWatchHistory should add movie to watch history`() = runTest {
        val movieId = 35L
        coEvery {
            watchHistoryLocalDataSource.upsertMovieToWatchHistory(
                any()
            )
        } just Runs

        val result = repository.addMovieToWatchHistory(movieId)

        assertThat(result).isEqualTo(Unit)

        coVerify(exactly = 1) {
            watchHistoryLocalDataSource.upsertMovieToWatchHistory(
                withArg {
                    assertThat(it.movieId).isEqualTo(movieId)
                }
            )
        }

    }

    @Test
    fun `getContinueWatchingMovies should return continue watching movies`() = runTest {
        // Given
        val page = 1
        val pageSize = 10
        val movieHistoryDtoList = listOf<MovieWatchHistoryDto>()
        val expectedMovies = listOf<MovieWatchHistory>()

        coEvery {
            watchHistoryLocalDataSource.getMoviesWatchHistory(page, pageSize)
        } returns flowOf(movieHistoryDtoList)

        // When
        val result = repository.getContinueWatchingMovies(page, pageSize).first()

        // Then
        assertThat(result).isEqualTo(expectedMovies)

        coVerify(exactly = 1) {
            watchHistoryLocalDataSource.getMoviesWatchHistory(page, pageSize)
        }
    }

    @Test
    fun `addTvShowToWatchHistory should add tvShow to watch history`() = runTest {
        val tvShow = 35L
        coEvery {
            watchHistoryLocalDataSource.upsertTvShowToWatchHistory(
                any()
            )

        } just Runs

        val result = repository.addTvShowToWatchHistory(tvShow)

        assertThat(result).isEqualTo(Unit)

        coVerify(exactly = 1) {
            watchHistoryLocalDataSource.upsertTvShowToWatchHistory(
                withArg {
                    assertThat(it.tvShowId).isEqualTo(tvShow)
                }
            )
        }

    }

    @Test
    fun `getContinueWatchingTvShow should return continue watching tvShow`() = runTest {
        // Given
        val page = 1
        val pageSize = 10
        val tvShowHistoryDtoList = listOf<TvShowWatchHistoryDto>()
        val expectedTvShow = listOf<TvShowWatchHistory>()

        coEvery {
            watchHistoryLocalDataSource.getTvShowsWatchHistory(page, pageSize)
        } returns flowOf(tvShowHistoryDtoList)

        // When
        val result = repository.getContinueWatchingTvShows(page, pageSize).first()

        // Then
        assertThat(result).isEqualTo(expectedTvShow)

        coVerify(exactly = 1) {
            watchHistoryLocalDataSource.getTvShowsWatchHistory(page, pageSize)
        }
    }


}

