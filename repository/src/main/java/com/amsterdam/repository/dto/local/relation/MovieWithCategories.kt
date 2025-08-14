package com.amsterdam.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto

data class MovieWithCategories(
    @Embedded val movie: MovieLocalDto,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "categoryId",
        associateBy = Junction(MovieCategoryCrossRefDto::class)
    )
    val categories: List<MovieCategoryLocalDto>
)