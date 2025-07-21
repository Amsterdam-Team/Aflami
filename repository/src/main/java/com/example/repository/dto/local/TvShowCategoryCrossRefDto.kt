package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TV_SHOW_CATEGORY_CROSS_REF_TABLE,
    primaryKeys = ["tvShowId", "categoryId", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = LocalTvShowDto::class,
            parentColumns = ["tvShowId", "storedLanguage"],
            childColumns = ["tvShowId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalTvShowCategoryDto::class,
            parentColumns = ["categoryId", "storedLanguage"],
            childColumns = ["categoryId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["tvShowId", "storedLanguage"]),
        Index(value = ["categoryId", "storedLanguage"])
    ]
)
data class TvShowCategoryCrossRefDto(
    val tvShowId: Long,
    val categoryId: Long,
    val storedLanguage: String
)
