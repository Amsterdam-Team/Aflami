package com.example.entity

import com.example.entity.category.TvShowGenre

data class TvShow(
    val id: Long,
    val name: String,
    val description: String,
    val posterUrl: String,
    val productionYear: UInt,
    val categories: List<TvShowGenre>,
    val rating: Float,
    val popularity: Double
)