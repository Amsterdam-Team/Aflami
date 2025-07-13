package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto

interface LocalCategoryDataSource {
    suspend fun upsertAllMovieCategories(categories: List<LocalMovieCategoryDto>)
    suspend fun upsertAllTvShowCategories(categories: List<LocalTvShowCategoryDto>)
    suspend fun getAllMovieCategories(): List<LocalMovieCategoryDto>
    suspend fun getAllTvShowCategories(): List<LocalTvShowCategoryDto>
}
