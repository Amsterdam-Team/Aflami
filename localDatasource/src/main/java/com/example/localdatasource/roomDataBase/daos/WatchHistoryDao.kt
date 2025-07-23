package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.WatchHistoryDto
import com.example.repository.dto.local.utils.DatabaseConstants

@Dao
interface WatchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchHistory(item: WatchHistoryDto)

    @Query("""
    SELECT *
    FROM ${DatabaseConstants.MOVIE_TABLE} AS m
    JOIN ${DatabaseConstants.WATCH_HISTORY_TABLE} AS w
    ON m.movieId = w.movieId
    ORDER BY w.lastWatchedTime DESC
""")
    suspend fun getContinueWatching(): List<LocalMovieDto>
}