package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(tableName = DatabaseContract.MOVIE_CATEGORY_INTEREST_TABLE)
data class LocalMovieCategoryInterestDto(
    @PrimaryKey val genre: MovieGenre,
    val interestCount: Int
)