package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "tv_category_cross_ref",
    primaryKeys = ["tvId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalTvShowDto::class,
            parentColumns = ["id"],
            childColumns = ["tvId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalCategoryDto::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TvShowCategoryCrossRefDto(
    val tvId: Long,
    val categoryId: Long
)