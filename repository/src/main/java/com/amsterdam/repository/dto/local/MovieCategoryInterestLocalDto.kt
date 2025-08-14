package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(tableName = DatabaseConstants.MOVIE_CATEGORY_INTEREST_TABLE)
data class MovieCategoryInterestLocalDto(
    @PrimaryKey val categoryId: Long,
    val interestCount: Int
)