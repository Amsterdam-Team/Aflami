package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.MOVIE_CATEGORY_TABLE,
)
data class MovieCategoryLocalDto(
    @PrimaryKey val categoryId: Long
)