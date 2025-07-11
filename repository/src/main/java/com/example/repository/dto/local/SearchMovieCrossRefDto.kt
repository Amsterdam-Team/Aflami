package com.example.repository.dto.local

import androidx.room.Entity

@Entity(
    tableName = "search_movie_cross_ref",
    primaryKeys = ["searchId", "movieId"]
)
data class SearchMovieCrossRefDto(
    val searchId: Long,
    val movieId: Long
)