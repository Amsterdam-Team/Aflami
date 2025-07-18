package com.example.entity

data class Season(
    val id: Long,
    val seasonNumber: Int,
    val episodeCount: Int,
    val episodes: List<Long>
)