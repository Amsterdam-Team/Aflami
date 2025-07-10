package com.example.repository.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.repository.dto.local.LocalCategoryDto

@Dao
interface LocalCategoryDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCategory(category: LocalCategoryDto)
}