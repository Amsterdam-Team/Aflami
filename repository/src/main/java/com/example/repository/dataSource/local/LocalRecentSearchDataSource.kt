package com.example.repository.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.repository.dto.local.SearchDto

@Dao
interface LocalRecentSearchDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSearch(search: SearchDto)

    @Query("SELECT DISTINCT searchKeyword FROM SearchDto ORDER BY rowid DESC")
    suspend fun getRecentSearches(): List<String>

    @Query("DELETE FROM SearchDto")
    suspend fun deleteAllSearches()

    @Query("DELETE FROM SearchDto WHERE searchKeyword = :keyword")
    suspend fun deleteSearchByKeyword(keyword: String)
}