package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Dao
interface TopRatedMovieDao {
    @Upsert
    suspend fun insertTopRatedMovies(movies: List<TopRatedMovieDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.TOP_RATED_MOVIE_TABLE} 
            WHERE dateAdded < :expirationTime AND storedLanguage = :storedLanguage
    """
    )
    suspend fun deleteAllExpiredTopRatedMovies(expirationTime: Instant, storedLanguage: String)
}
