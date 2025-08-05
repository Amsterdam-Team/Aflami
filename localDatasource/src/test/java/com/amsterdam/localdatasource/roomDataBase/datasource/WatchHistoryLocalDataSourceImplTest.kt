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
import kotlinx.datetime.Clock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WatchHistoryLocalDataSourceImplTest {

    private lateinit var watchHistoryDao: WatchHistoryDao
    private lateinit var dataSource: WatchHistoryLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        watchHistoryDao = mockk(relaxed = true)
        dataSource = WatchHistoryLocalDataSourceImpl(watchHistoryDao)
    }

    @Test
    fun `addMovieToWatchHistory should call addMovieToWatchHistory in the watchHistoryDao`() =
        runTest {
            //Given
            val movieWatchHistoryDto = MovieWatchHistoryDto(
                movieId = 1,
                watchedDate = Clock.System.now()
            )

            //When
            dataSource.addMovieToWatchHistory(movieWatchHistoryDto)

            //Then
            coVerify(exactly = 1) { watchHistoryDao.addMovieToWatchHistory(movieWatchHistoryDto) }
        }

    @Test
    fun `getMoviesWatchHistory should call the dao with correct offset`() = runTest {
        //Give
        val page = 2
        val pageSize = 20
        val offset = (page - 1) * pageSize

        //When
        dataSource.getMoviesWatchHistory(page, pageSize)

        //Then
        coVerify(exactly = 1) { watchHistoryDao.getMoviesWatchHistory(offset, pageSize) }
    }

    @Test
    fun `getMoviesWatchHistory should return the correct flow`() = runTest {
        //Give
        val movieWatchHistoryDto = MovieWatchHistoryDto(
            movieId = 1,
            watchedDate = Clock.System.now()
        )
        val movieWatchHistoryFlow = flow { emit(listOf(movieWatchHistoryDto)) }
        coEvery {
            watchHistoryDao.getMoviesWatchHistory(
                any(),
                any()
            )
        } returns movieWatchHistoryFlow

        //When
        val result = dataSource.getMoviesWatchHistory(1, 20)

        //Then
        assertThat(result.first()).isEqualTo(listOf(movieWatchHistoryDto))

    }

    @Test
    fun `addTvShowToWatchHistory should call addTvShowToWatchHistory in the watchHistoryDao`() =
        runTest {
            // Given
            val tvShowWatchHistoryDto = TvShowWatchHistoryDto(
                tvShowId = 101,
                watchedDate = Clock.System.now()
            )

            // When
            dataSource.addTvShowToWatchHistory(tvShowWatchHistoryDto)

            // Then
            coVerify(exactly = 1) {
                watchHistoryDao.addTvShowToWatchHistory(tvShowWatchHistoryDto)
            }
        }

    @Test
    fun `getTvShowsWatchHistory should call the dao with correct offset`() = runTest {
        // Given
        val page = 3
        val pageSize = 15
        val offset = (page - 1) * pageSize

        // When
        dataSource.getTvShowsWatchHistory(page, pageSize)

        // Then
        coVerify(exactly = 1) {
            watchHistoryDao.getTvShowsWatchHistory(offset, pageSize)
        }
    }

    @Test
    fun `getTvShowsWatchHistory should return the correct flow`() = runTest {
        // Given
        val currentDateTime = Clock.System.now()
        val tvShowWatchHistoryFlow = flow {
            emit(listOf(TvShowWatchHistoryDto(101, currentDateTime)))
        }
        coEvery {
            watchHistoryDao.getTvShowsWatchHistory(
                any(),
                any()
            )
        } returns tvShowWatchHistoryFlow

        // When
        val result = dataSource.getTvShowsWatchHistory(1, 20)

        // Then
        assertThat(result.first()).isEqualTo(listOf(TvShowWatchHistoryDto(101, currentDateTime)))
    }

}


