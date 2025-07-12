package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "movie_category_cross_ref",
    primaryKeys = ["movieId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalCategoryDto::class,
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
