package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TV_SHOW_CATEGORY_TABLE,
    primaryKeys = ["categoryId", "storedLanguage"],
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalTvShowCategoryDto(
    val categoryId: Long,
    val storedLanguage: String,
    val name: String
)