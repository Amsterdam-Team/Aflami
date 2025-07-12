package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalCategoryDto

@Dao
interface CategoryDao {

    @Upsert
    suspend fun upsertCategory(category: LocalCategoryDto)

    @Upsert
    suspend fun upsertAllCategories(categories: List<LocalCategoryDto>)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<LocalCategoryDto>
}