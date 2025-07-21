package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.repository.dto.local.utils.DatabaseConstants
import com.example.repository.dto.local.utils.SearchType

@Entity(
    tableName = DatabaseConstants.SEARCH_MOVIE_CROSS_REF_TABLE,
    primaryKeys = ["searchKeyword", "searchType", "movieId", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["movieId", "storedLanguage"],
            childColumns = ["movieId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SearchMovieCrossRefDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val movieId: Long,
    val storedLanguage: String,
)