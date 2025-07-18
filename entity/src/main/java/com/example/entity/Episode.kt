package com.example.entity

import kotlinx.datetime.LocalDate

data class Episode(
    val id: Long,
    val title: String,
    val episodeNumber: Int,
    val description: String,
    val stillUrl: String,
    val rating: Float,
    val airDate: LocalDate,
    val seasonNumber: Int,
    val runtime: Int,
)
