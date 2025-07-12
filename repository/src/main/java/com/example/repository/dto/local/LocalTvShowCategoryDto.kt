package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tv_show_categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalTvShowCategoryDto(
    @PrimaryKey val id: Long,
    val name: String
)