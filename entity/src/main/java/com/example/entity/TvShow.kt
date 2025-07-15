package com.example.entity

import com.example.entity.common.Categorizable
import com.example.entity.common.PopularitySortable
import com.example.entity.common.RatingFilterable

data class TvShow(
    val id: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    override val categories: List<Category>,
    override val rating: Float,
    override val popularity: Double
) : PopularitySortable, Categorizable, RatingFilterable