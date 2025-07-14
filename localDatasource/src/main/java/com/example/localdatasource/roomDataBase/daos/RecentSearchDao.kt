package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.DatabaseContract
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

@Dao
interface RecentSearchDao {
    @Upsert
    suspend fun upsertRecentSearch(search: LocalSearchDto)

    @Query("SELECT* FROM ${DatabaseContract.RECENT_SEARCH_TABLE} ORDER BY rowid DESC")
    suspend fun getRecentSearches(): List<LocalSearchDto>

    @Query("DELETE FROM ${DatabaseContract.RECENT_SEARCH_TABLE}")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM ${DatabaseContract.RECENT_SEARCH_TABLE} WHERE expireDate <= :expireDate")
    suspend fun deleteAllExpiredSearches(expireDate: Instant)

    @Transaction
    @Query("SELECT * FROM ${DatabaseContract.RECENT_SEARCH_TABLE} WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun getSearchByKeywordAndType(
        keyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    ): LocalSearchDto?

    @Query("DELETE FROM ${DatabaseContract.RECENT_SEARCH_TABLE} WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun deleteSearchByKeyword(
        keyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    )

    @Query("DELETE FROM ${DatabaseContract.SEARCH_MOVIE_CROSS_REF_TABLE} WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun deleteSearchMovieCrossRefByKeyword(
        keyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    )
}