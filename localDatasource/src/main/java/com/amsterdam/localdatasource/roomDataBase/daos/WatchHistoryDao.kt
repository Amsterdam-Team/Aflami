package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
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

    @Query("SELECT * FROM ${DatabaseConstants.MOVIE_WATCH_HISTORY_TABLE} LIMIT :limit OFFSET :offset")
     fun getMoviesWatchHistory(
        offset: Int,
        limit: Int
    ): Flow<List<MovieWatchHistoryDto>>

    @Query("SELECT * FROM ${DatabaseConstants.TV_WATCH_HISTORY_TABLE} LIMIT :limit OFFSET :offset")
     fun getTvShowsWatchHistory(
        offset: Int,
        limit: Int,
    ): Flow<List<TvShowWatchHistoryDto>>
}