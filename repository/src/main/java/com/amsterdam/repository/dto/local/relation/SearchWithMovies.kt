package com.amsterdam.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.SearchMovieCrossRefDto

data class SearchWithMovies(
    @Embedded val search: LocalSearchDto,
    @Relation(
        parentColumn = "searchKeyword",
        entityColumn = "movieId",
        associateBy = Junction(SearchMovieCrossRefDto::class)
    )
    val movies: List<LocalMovieDto>
)
