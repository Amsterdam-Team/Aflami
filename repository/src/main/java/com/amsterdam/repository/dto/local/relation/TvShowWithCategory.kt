package com.amsterdam.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto

data class TvShowWithCategory(
    @Embedded val tvShow: LocalTvShowDto,
    @Relation(
        parentColumn = "tvShowId",
        entityColumn = "categoryId",
        associateBy = Junction(TvShowCategoryCrossRefDto::class)
    )
    val categories: List<LocalTvShowCategoryDto>
)