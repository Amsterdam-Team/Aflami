package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto

interface LocalCategoryDataSource {
    suspend fun insertOrReplaceAllMovieCategories(categories: List<LocalMovieCategoryDto>)
    suspend fun insertOrReplaceAllTvShowCategories(categories: List<LocalTvShowCategoryDto>)
    suspend fun getAllMovieCategories(): List<LocalMovieCategoryDto>
    suspend fun getAllTvShowCategories(): List<LocalTvShowCategoryDto>
}
