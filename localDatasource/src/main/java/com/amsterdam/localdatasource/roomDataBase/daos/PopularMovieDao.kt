package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.PopularMovieDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface PopularMovieDao {
    @Upsert
    suspend fun insertPopularMovies(movies: List<PopularMovieDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.POPULAR_MOVIE_TABLE}
            WHERE dateAdded < :expirationTime and storedLanguage = :storedLanguage
        """
    )
    suspend fun deleteExpiredPopularMovies(expirationTime: Long, storedLanguage: String)
}
