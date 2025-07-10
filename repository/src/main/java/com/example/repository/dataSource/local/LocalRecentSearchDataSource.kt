package com.example.repository.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.repository.dto.local.SearchDto

@Dao
interface LocalRecentSearchDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSearch(search: SearchDto)

    @Query("SELECT DISTINCT searchKeyword FROM search ORDER BY rowid DESC")
    suspend fun getRecentSearches(): List<String>

    @Query("DELETE FROM search")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM search WHERE searchKeyword = :keyword")
    suspend fun deleteSearchByKeyword(keyword: String)
}