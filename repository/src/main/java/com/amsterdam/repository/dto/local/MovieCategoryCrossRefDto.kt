package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.MOVIE_CATEGORY_CROSS_REF_TABLE,
    primaryKeys = ["movieId", "categoryId", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = MovieLocalDto::class,
            parentColumns = ["movieId", "storedLanguage"],
            childColumns = ["movieId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovieCategoryLocalDto::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["movieId", "storedLanguage"]),
        Index(value = ["categoryId", "storedLanguage"])
    ]
)
data class MovieCategoryCrossRefDto(
    val movieId: Long,
    val categoryId: Long,
    val storedLanguage: String
)