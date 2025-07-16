package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(
    tableName = DatabaseContract.TV_SHOW_CATEGORY_TABLE,
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalTvShowCategoryDto(
    @PrimaryKey val categoryId: Long,
    val name: String
)