package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TV_SHOW_CATEGORY_CROSS_REF_TABLE,
    primaryKeys = ["tvShowId", "categoryId", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = TvShowLocalDto::class,
            parentColumns = ["tvShowId", "storedLanguage"],
            childColumns = ["tvShowId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TvShowCategoryLocalDto::class,
            parentColumns = ["categoryId", ],
            childColumns = ["categoryId"],
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