package com.amsterdam.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto

data class TvShowWithCategories(
    @Embedded val tvShow: TvShowLocalDto,
    @Relation(
        parentColumn = "tvShowId",
        entityColumn = "categoryId",
        associateBy = Junction(TvShowCategoryCrossRefDto::class)
    )
    val categories: List<TvShowCategoryLocalDto>
)