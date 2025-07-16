package com.example.entity

import com.example.entity.category.TvShowCategoryType
import com.example.entity.common.PopularitySortable
import com.example.entity.common.RatingFilterable

data class TvShow(
    val id: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val categories: List<TvShowCategoryType>,
    override val rating: Float,
    override val popularity: Double
) : PopularitySortable, RatingFilterable