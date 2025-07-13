package com.amsterdam.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto

data class CategoryWithMovies(
    @Embedded val category: LocalMovieCategoryDto,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "movieId",
        associateBy = Junction(MovieCategoryCrossRefDto::class)
    )
    val movies: List<LocalMovieDto>
)