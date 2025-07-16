package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(
    tableName = DatabaseContract.MOVIE_CATEGORY_TABLE,
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalMovieCategoryDto(
    @PrimaryKey(autoGenerate = false) val categoryId: Long,
    val name: String
)