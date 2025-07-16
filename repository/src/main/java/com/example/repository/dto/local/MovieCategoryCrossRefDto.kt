package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(
    tableName = DatabaseContract.MOVIE_CATEGORY_CROSS_REF_TABLE,
    primaryKeys = ["movieId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalMovieCategoryDto::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieCategoryCrossRefDto(
    val movieId: Long,
    val categoryId: Long
)
