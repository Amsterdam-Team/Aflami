package com.amsterdam.localdatasource.daos

import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.days

class WatchHistoryDaoTest : BaseDaoTest() {
    private lateinit var watchHistoryDao: WatchHistoryDao

    @BeforeEach
    fun setup() {
        watchHistoryDao = aflamiDatabase.watchHistoryDao()
    }

    @Test
    fun upsertMovieToWatchHistory_shouldInsertMovieToWatchHistory_whenMoviesWatchHistoryNotExists() =
        runTest {
            watchHistoryDao.upsertMovieToWatchHistory(movieWatchHistoryDto)
            val result = watchHistoryDao.getMoviesWatchHistory(0, 1)

            assertThat(result.first()).containsExactly(movieWatchHistoryDto)
        }

    @Test
    fun upsertMovieToWatchHistory_shouldUpdateMovieToWatchHistory_whenMoviesWatchHistoryExists() =
        runTest {
            watchHistoryDao.upsertMovieToWatchHistory(movieWatchHistoryDto)

            watchHistoryDao.upsertMovieToWatchHistory(updatedMovieWatchHistoryDto)
            val result = watchHistoryDao.getMoviesWatchHistory(0, 1)

            assertThat(result.first()).containsExactly(updatedMovieWatchHistoryDto)
        }

    @Test
    fun upsertTvShowToWatchHistory_shouldInsertTvShowToWatchHistory_whenTvShowWatchHistoryNotExists() =
        runTest {
            watchHistoryDao.upsertTvShowToWatchHistory(tvShowWatchHistoryDto)
            val result = watchHistoryDao.getTvShowsWatchHistory(0, 1)

            assertThat(result.first()).containsExactly(tvShowWatchHistoryDto)
        }

    @Test
    fun upsertTvShowToWatchHistory_shouldUpdateTvShowToWatchHistory_whenTvShowWatchHistoryExists() =
        runTest {
            watchHistoryDao.upsertTvShowToWatchHistory(tvShowWatchHistoryDto)

            watchHistoryDao.upsertTvShowToWatchHistory(updatedTvShowWatchHistoryDto)
            val result = watchHistoryDao.getTvShowsWatchHistory(0, 1)

            assertThat(result.first()).containsExactly(updatedTvShowWatchHistoryDto)
        }

    @Test
    fun getMoviesWatchHistory_shouldReturnMoviesWatchHistory_whenMoviesWatchHistoryExists() =
        runTest {
            watchHistoryDao.upsertMovieToWatchHistory(movieWatchHistoryDto)

            val result = watchHistoryDao.getMoviesWatchHistory(0, 1)

            assertThat(result.first()).containsExactly(movieWatchHistoryDto)
        }

    @Test
    fun getMoviesWatchHistory_shouldReturnEmptyList_whenMoviesWatchHistoryNotExists() = runTest {
        val result = watchHistoryDao.getMoviesWatchHistory(0, 1)

        assertThat(result.firstOrNull()).isEqualTo(emptyList<MovieWatchHistoryDto>())
    }

    @Test
    fun getTvShowsWatchHistory_shouldReturnTvShowsWatchHistory_whenTvShowWatchHistoryExists() =
        runTest {
            watchHistoryDao.upsertTvShowToWatchHistory(tvShowWatchHistoryDto)

            val result = watchHistoryDao.getTvShowsWatchHistory(0, 1)

            assertThat(result.first()).containsExactly(tvShowWatchHistoryDto)
        }

    @Test
    fun getTvShowsWatchHistory_shouldReturnTvShowsWatchHistory_whenTvShowWatchHistoryNotExists() =
        runTest {
            val result = watchHistoryDao.getTvShowsWatchHistory(0, 1)

            assertThat(result.firstOrNull()).isEqualTo(emptyList<TvShowWatchHistoryDto>())
        }


}

private val movieWatchHistoryDto = MovieWatchHistoryDto(
    movieId = 1
)

private val updatedMovieWatchHistoryDto = movieWatchHistoryDto.copy(
    watchedDate = movieWatchHistoryDto.watchedDate + 3.days
)

private val tvShowWatchHistoryDto = TvShowWatchHistoryDto(
    tvShowId = 1
)

private val updatedTvShowWatchHistoryDto = tvShowWatchHistoryDto.copy(
    watchedDate = tvShowWatchHistoryDto.watchedDate + 3.days
)