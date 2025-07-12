package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.SearchType

@Entity(
    tableName = "search_movie_cross_ref",
    primaryKeys = ["searchKeyword", "searchType", "movieId"]
)
data class SearchMovieCrossRefDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val movieId: Long
)