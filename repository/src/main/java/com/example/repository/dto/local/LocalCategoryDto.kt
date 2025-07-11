package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalCategoryDto(
    @PrimaryKey val categoryId: Long,
    val name: String
)