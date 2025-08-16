package com.amsterdam.entity

import com.amsterdam.entity.category.MovieGenre
import kotlinx.datetime.LocalDate

data class Movie(
    val id: Long,
    val name: String,
    val description: String,
    val posterUrl: String,
    val releaseDate: LocalDate?,
    val categories: List<MovieGenre>,
    val rating: Float,
    val popularity: Double,
    val originCountry: String,
    val runTimeInMinutes: Int,
    val videoUrl: String? = null,
)