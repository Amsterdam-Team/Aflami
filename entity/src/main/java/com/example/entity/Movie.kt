package com.example.entity

import com.example.entity.common.Categorizable
import com.example.entity.common.PopularitySortable
import com.example.entity.common.RatingFilterable

data class Movie(
    val id: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    override val rating: Float,
    override val popularity: Double,
    val originCountry: String,
    val movieLength: Int,
    val hasVideo : Boolean,
    override val categories: List<Category>
) : PopularitySortable, Categorizable, RatingFilterable