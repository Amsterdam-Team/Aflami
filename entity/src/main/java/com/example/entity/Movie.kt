package com.example.entity

import com.example.entity.category.MovieCategoryType
import com.example.entity.common.PopularitySortable
import com.example.entity.common.RatingFilterable

data class Movie(
    val id: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val categories: List<MovieCategoryType>,
    override val rating: Float,
    override val popularity: Double,
) : PopularitySortable, RatingFilterable