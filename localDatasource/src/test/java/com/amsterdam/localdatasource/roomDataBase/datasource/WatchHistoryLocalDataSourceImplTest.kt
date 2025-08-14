package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test

class WatchHistoryLocalDataSourceImplTest {

    private val watchHistoryDao by lazy { mockk<WatchHistoryDao>(relaxed = true) }
    private val dataSource by lazy { WatchHistoryLocalDataSourceImpl(watchHistoryDao) }

    @Test
    fun `upsertMovieToWatchHistory should call addMovieToWatchHistory in the dao`() = runTest {
        dataSource.upsertMovieToWatchHistory(movieWatchHistoryDto)

        coVerify(exactly = 1) { watchHistoryDao.upsertMovieToWatchHistory(movieWatchHistoryDto) }
    }

    @Test
    fun `getMoviesWatchHistory should call the dao with correct offset`() = runTest {
        dataSource.getMoviesWatchHistory(page, pageSize)

        coVerify(exactly = 1) { watchHistoryDao.getMoviesWatchHistory(offset, pageSize) }
    }

    @Test
    fun `getMoviesWatchHistory should return list of movieWatchHistoryDto when data returned`() =
        runTest {
            coEvery {
                watchHistoryDao.getMoviesWatchHistory(
                    any(),
                    any()
                )
            } returns movieWatchHistoryFlow

            val result = dataSource.getMoviesWatchHistory(page, pageSize)

            assertThat(result.first()).isEqualTo(listOf(movieWatchHistoryDto))
        }

    @Test
    fun `upsertTvShowToWatchHistory should call addTvShowToWatchHistory in the dao`() = runTest {
        dataSource.upsertTvShowToWatchHistory(tvShowWatchHistoryDto)

        coVerify(exactly = 1) { watchHistoryDao.upsertTvShowToWatchHistory(tvShowWatchHistoryDto) }
    }

    @Test
    fun `getTvShowsWatchHistory should call the dao with correct offset`() = runTest {
        dataSource.getTvShowsWatchHistory(page, pageSize)

        coVerify(exactly = 1) { watchHistoryDao.getTvShowsWatchHistory(offset, pageSize) }
    }

    @Test
    fun `getTvShowsWatchHistory should return list of tvShowWatchHistoryDto when data returned`() =
        runTest {
            coEvery {
                watchHistoryDao.getTvShowsWatchHistory(
                    any(),
                    any()
                )
            } returns tvShowWatchHistoryFlow

            val result = dataSource.getTvShowsWatchHistory(page, pageSize)

            assertThat(result.first()).isEqualTo(listOf(tvShowWatchHistoryDto))
        }
}

private val movieWatchHistoryDto = MovieWatchHistoryDto(
    movieId = 1,
    watchedDate = Instant.parse("2023-01-01T00:00:00Z")
)

private const val page = 2
private const val pageSize = 20
private const val offset = (page - 1) * pageSize

private val movieWatchHistoryFlow = flow { emit(listOf(movieWatchHistoryDto)) }

private val tvShowWatchHistoryDto = TvShowWatchHistoryDto(
    tvShowId = 101,
    watchedDate = Instant.parse("2023-01-01T00:00:00Z")
)

private val tvShowWatchHistoryFlow = flow { emit(listOf(tvShowWatchHistoryDto)) }