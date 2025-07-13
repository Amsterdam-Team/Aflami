package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "tv_show_category_cross_ref",
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
