package com.example.entity

data class Movie(
    val id: Long,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val rating: Float,
    val popularity: Double,
    val originCountry: String,
    val movieLength: Int,
    val hasVideo : Boolean
) : PopularitySortable, Categorizable, RatingFilterable