package com.example.repository.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalCategoryDto

@Dao
interface LocalCategoryDataSource {
    @Upsert
    suspend fun insertOrReplaceCategory(category: LocalCategoryDto)

    @Upsert
    suspend fun addAllCategories(categories: List<LocalCategoryDto>)

    @Query("Select * from categories")
    suspend fun getAllCategories(): List<LocalCategoryDto>
}