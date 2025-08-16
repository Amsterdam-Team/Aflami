package com.amsterdam.entity

import com.amsterdam.entity.category.TvShowGenre
import kotlinx.datetime.LocalDate

data class TvShow(
    val id: Long,
    val name: String,
    val description: String,
    val posterUrl: String,
    val airDate: LocalDate?,
    val categories: List<TvShowGenre>,
    val rating: Float,
    val popularity: Double,
    val seasonCount: Int,
    val originCountry: String,
    val videoUrl: String? = null,
)