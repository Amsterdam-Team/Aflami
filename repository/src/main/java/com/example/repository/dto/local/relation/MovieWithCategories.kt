package com.example.repository.dto.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.repository.dto.local.LocalCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.MovieCategoryCrossRefDto

data class MovieWithCategories(
    @Embedded val movie: LocalMovieDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MovieCategoryCrossRefDto::class,
            parentColumn = "movieId",
            entityColumn = "categoryId"
        )
    )
    val categories: List<LocalCategoryDto>
)