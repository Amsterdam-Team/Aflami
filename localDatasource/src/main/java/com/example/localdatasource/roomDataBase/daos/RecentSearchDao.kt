package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

@Dao
interface RecentSearchDao {
    @Upsert
    suspend fun upsertRecentSearch(search: LocalSearchDto)

    @Query("SELECT* FROM SearchDto ORDER BY rowid DESC")
    suspend fun getRecentSearches(): List<LocalSearchDto>

    @Query("DELETE FROM SearchDto")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM SearchDto WHERE expireDate <= :expireDate")
    suspend fun deleteAllExpiredSearches(expireDate: Instant)

    @Query("SELECT * FROM SearchDto WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun getSearchByKeywordAndType(
        keyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    ): LocalSearchDto?

    @Query("DELETE FROM SearchDto WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun deleteSearchByKeyword(
        keyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    )

    @Query("DELETE FROM search_movie_cross_ref WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun deleteSearchMovieCrossRefByKeyword(
        keyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    )
}