package com.example.localdatasource.roomDatabase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalCategoryDto

@Dao
interface CategoryDao {

    @Upsert
    suspend fun insertOrReplaceCategory(category: LocalCategoryDto)

    @Upsert
    suspend fun insertOrReplaceAll(categories: List<LocalCategoryDto>)

    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<LocalCategoryDto>
}