package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.repository.dto.local.utils.DatabaseContract


@Entity(
    tableName = DatabaseContract.TV_SHOW_CATEGORY_CROSS_REF_TABLE,
    primaryKeys = ["tvShowId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalTvShowDto::class,
            parentColumns = ["tvShowId"],
            childColumns = ["tvShowId"],
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
data class TvShowCategoryCrossRefDto(
    val tvShowId: Long,
    val categoryId: Long
)
