package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.SearchLocalDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface RecentSearchDao {
    @Upsert
    suspend fun upsertRecentSearch(search: SearchLocalDto)

    @Query(
    """
        SELECT *
        FROM ${DatabaseConstants.RECENT_SEARCH_TABLE}
        ORDER BY dateAdded DESC
    """
    )
    suspend fun getRecentSearches(): List<SearchLocalDto>

    @Query("DELETE FROM ${DatabaseConstants.RECENT_SEARCH_TABLE}")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM ${DatabaseConstants.RECENT_SEARCH_TABLE} WHERE searchKeyword = :keyword ")
    suspend fun deleteSearchByKeyword(
        keyword: String
    )
}