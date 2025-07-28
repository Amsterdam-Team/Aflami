package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {
    @Upsert()
    suspend fun addMovieToWatchHistory(item: MovieWatchHistoryDto)

    @Upsert()
    suspend fun addTvShowToWatchHistory(item: TvShowWatchHistoryDto)

    @Query(
        """
    SELECT *
    FROM ${DatabaseConstants.MOVIE_TABLE} AS m
    JOIN ${DatabaseConstants.MOVIE_WATCH_HISTORY_TABLE} AS w
    ON m.movieId = w.movieId
    ORDER BY w.lastWatchedTime DESC
    """
    )
    fun getMovieContinueWatching(): Flow<List<LocalMovieDto>>


    @Query(
        """
    SELECT *
    FROM ${DatabaseConstants.TV_SHOW_TABLE} AS t
    JOIN ${DatabaseConstants.TV_WATCH_HISTORY_TABLE} AS w
    ON t.tvShowId = w.tvShowId
    ORDER BY w.lastWatchedTime DESC
    """
    )
    fun getTvShowContinueWatching(): Flow<List<LocalTvShowDto>>
}