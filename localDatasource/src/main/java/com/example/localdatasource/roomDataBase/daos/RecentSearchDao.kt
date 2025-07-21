package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.DatabaseConstants
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

@Dao
interface RecentSearchDao {
    @Upsert
    suspend fun upsertRecentSearch(search: LocalSearchDto)

    @Query("""
        SELECT searchKeyword, searchType, storedLanguage, expireDate
        FROM ${DatabaseConstants.RECENT_SEARCH_TABLE}
        WHERE searchType = :searchType
        GROUP BY searchKeyword
        ORDER BY expireDate DESC
    """)
    suspend fun getRecentSearches(searchType: SearchType): List<LocalSearchDto>

    @Query("DELETE FROM ${DatabaseConstants.RECENT_SEARCH_TABLE}")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM ${DatabaseConstants.RECENT_SEARCH_TABLE} WHERE expireDate <= :expireDate")
    suspend fun deleteAllExpiredSearches(expireDate: Instant)

    @Transaction
    @Query("SELECT * FROM ${DatabaseConstants.RECENT_SEARCH_TABLE} WHERE searchKeyword = :keyword and searchType = :searchType and storedLanguage = :storedLanguage")
    suspend fun getSearchByKeywordAndType(
        keyword: String, searchType: SearchType, storedLanguage: String
    ): LocalSearchDto?

    @Query("DELETE FROM ${DatabaseConstants.RECENT_SEARCH_TABLE} WHERE searchKeyword = :keyword and searchType = :searchType and storedLanguage = :storedLanguage")
    suspend fun deleteSearchByKeyword(
        keyword: String, searchType: SearchType, storedLanguage: String
    )

    @Query("DELETE FROM ${DatabaseConstants.SEARCH_MOVIE_CROSS_REF_TABLE} WHERE searchKeyword = :keyword and searchType = :searchType and storedLanguage = :storedLanguage")
    suspend fun deleteSearchMovieCrossRefByKeyword(
        keyword: String, searchType: SearchType, storedLanguage: String
    )
}