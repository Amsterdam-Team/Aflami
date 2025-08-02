package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.PopularTvShowDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Dao
interface PopularTvShowsDao {
    @Upsert
    suspend fun insertPopularTvShows(tvShows: List<PopularTvShowDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.POPULAR_TV_SHOW_TABLE}
            WHERE dateAdded < :expirationTime and storedLanguage = :storedLanguage
        """
    )
    suspend fun deleteExpiredPopularTvShows(expirationTime: Instant, storedLanguage: String)
}
