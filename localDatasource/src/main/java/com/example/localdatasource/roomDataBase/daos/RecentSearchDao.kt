package com.example.localdatasource.roomDatabase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.SearchDto
import com.example.repository.dto.local.utils.SearchType

@Dao
interface RecentSearchDao {
    @Upsert
    suspend fun insertOrReplaceSearch(search: SearchDto)

    @Query("SELECT DISTINCT searchKeyword FROM SearchDto ORDER BY rowid DESC")
    suspend fun getRecentSearches(): List<String>

    @Query("DELETE FROM SearchDto")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM SearchDto WHERE searchKeyword = :keyword and searchType = :searchType")
    suspend fun deleteSearchByKeyword(keyword: String,searchType: SearchType = SearchType.BY_KEYWORD)
}