package com.example.entity

import com.example.entity.common.PopularitySortable

data class TvShow(
    val id: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val categories: List<Category>,
    val rating: Float,
    override val popularity: Double
) : PopularitySortable