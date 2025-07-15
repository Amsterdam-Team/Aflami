package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.DatabaseContract
import com.example.repository.dto.local.utils.SearchType

@Entity(
    tableName = DatabaseContract.SEARCH_MOVIE_CROSS_REF_TABLE,
    primaryKeys = ["searchKeyword", "searchType", "movieId"]
)
data class SearchMovieCrossRefDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val movieId: Long
)