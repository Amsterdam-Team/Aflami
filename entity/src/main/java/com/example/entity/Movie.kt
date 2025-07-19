package com.example.entity

import com.example.entity.category.MovieGenre

data class Movie(
    val id: Long,
    val name: String,
    val description: String,
    val posterUrl: String,
    val productionYear: UInt,
    val categories: List<MovieGenre>,
    val rating: Float,
    val popularity: Double,
    val originCountry: String,
    val runTime: Int,
    val hasVideo : Boolean,
)