package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Dao
interface TopRatedTvShowDao {

    @Upsert
    suspend fun insertTopRatedTvShows(tvShows: List<TopRatedTvShowDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.TOP_RATED_TV_SHOW_TABLE} 
            WHERE dateAdded < :expirationTime AND storedLanguage = :storedLanguage
    """
    )
    suspend fun deleteAllExpiredTopRatedTvShows(expirationTime: Instant, storedLanguage: String)
}
