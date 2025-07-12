package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_category_cross_ref",
    primaryKeys = ["movieId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalMovieCategoryDto::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("movieId"), Index("categoryId")]
)
data class MovieCategoryCrossRefDto(
    val movieId: Long,
    val categoryId: Long ,
)
