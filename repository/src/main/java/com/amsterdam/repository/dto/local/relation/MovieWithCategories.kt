package com.amsterdam.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto

data class MovieWithCategories(
    @Embedded val movie: LocalMovieDto,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "categoryId",
        associateBy = Junction(MovieCategoryCrossRefDto::class)
    )
    val categories: List<LocalMovieCategoryDto>
)
