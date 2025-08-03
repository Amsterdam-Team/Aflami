package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface RecentSearchDao {
    @Upsert
    suspend fun upsertRecentSearch(search: LocalSearchDto)

    @Query("""
        SELECT searchKeyword
        FROM ${DatabaseConstants.RECENT_SEARCH_TABLE}
    """)
    suspend fun getRecentSearches(): List<LocalSearchDto>

    @Query("DELETE FROM ${DatabaseConstants.RECENT_SEARCH_TABLE}")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM ${DatabaseConstants.RECENT_SEARCH_TABLE} WHERE searchKeyword = :keyword")
    suspend fun deleteSearchByKeyword(
        keyword: String
    )
}