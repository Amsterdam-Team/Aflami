package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.relation.SearchWithMovies
import com.example.repository.dto.local.utils.SearchType

@Dao
interface RecentSearchDao {
    @Upsert
    suspend fun insertOrReplaceSearch(search: LocalSearchDto)

    @Query("SELECT DISTINCT searchKeyword FROM SearchDto ORDER BY rowid DESC")
    suspend fun getRecentSearches(): List<String>

    @Query("DELETE FROM SearchDto")
    suspend fun deleteAllSearches()

    @Query("SELECT * FROM SearchDto WHERE searchKeyword = :keyword")
    suspend fun getSearchInfo(keyword: String): LocalSearchDto?

    @Query("SELECT * FROM SearchDto WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun getSearchByKeyword(
        keyword: String, searchType: SearchType = SearchType.BY_KEYWORD
    ): List<SearchWithMovies>

    @Query("DELETE FROM SearchDto WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun deleteSearchByKeyword(
        keyword: String,
        searchType: SearchType = SearchType.BY_KEYWORD
    )
}