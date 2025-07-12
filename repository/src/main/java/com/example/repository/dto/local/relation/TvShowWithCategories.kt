package com.example.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.repository.dto.local.LocalCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.TvShowCategoryCrossRefDto

data class TvShowWithCategories(
    @Embedded val tvShow: LocalTvShowDto,
    @Relation(
        parentColumn = "tvId",
        entityColumn = "categoryId",
        associateBy = Junction(TvShowCategoryCrossRefDto::class)
    )
    val categories: List<LocalCategoryDto>
)