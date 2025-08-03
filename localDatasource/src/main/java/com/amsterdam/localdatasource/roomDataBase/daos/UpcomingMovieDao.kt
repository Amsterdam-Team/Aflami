package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.UpcomingMovieDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Dao
interface UpcomingMovieDao {
    @Upsert
    suspend fun insertUpcomingMovies(movies: List<UpcomingMovieDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.UPCOMING_MOVIE_TABLE}
            WHERE dateAdded < :expirationTime and storedLanguage = :storedLanguage
        """
    )
    suspend fun deleteExpiredUpcomingMovies(expirationTime: Instant, storedLanguage: String)
}