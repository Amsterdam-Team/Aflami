package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalMovieCategoryDto(
    @PrimaryKey(autoGenerate = false) val categoryId: Long,
    val name: String
)