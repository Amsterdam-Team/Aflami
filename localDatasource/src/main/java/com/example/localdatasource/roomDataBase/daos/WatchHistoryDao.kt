package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.WatchHistoryDto
import com.example.repository.dto.local.utils.DatabaseConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {
    @Upsert()
    suspend fun addToWatchHistory(item: WatchHistoryDto)

    @Query("""
    SELECT *
    FROM ${DatabaseConstants.MOVIE_TABLE} AS m
    JOIN ${DatabaseConstants.WATCH_HISTORY_TABLE} AS w
    ON m.movieId = w.movieId
    ORDER BY w.lastWatchedTime DESC
""")
    fun getContinueWatching(): Flow<List<LocalMovieDto>>
}