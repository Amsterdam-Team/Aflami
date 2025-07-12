package com.example.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.SearchMovieCrossRefDto

data class SearchWithMovies(
    @Embedded val search: LocalSearchDto,
    @Relation(
        parentColumn = "\"searchKeyword\", \"searchType\"",
        entityColumn = "movieId",
        associateBy = Junction(SearchMovieCrossRefDto::class)
    )
    val movies: List<LocalMovieDto>
)
