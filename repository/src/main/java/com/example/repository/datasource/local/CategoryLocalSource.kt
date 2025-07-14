package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto

interface CategoryLocalSource {
    suspend fun upsertMovieCategories(categories: List<LocalMovieCategoryDto>)
    suspend fun getMovieCategories(): List<LocalMovieCategoryDto>

    suspend fun upsertTvShowCategories(categories: List<LocalTvShowCategoryDto>)
    suspend fun getTvShowCategories(): List<LocalTvShowCategoryDto>
}
