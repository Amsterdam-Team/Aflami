package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto

interface CategoryLocalSource {
    suspend fun upsertMovieCategories(categories: List<LocalMovieCategoryDto>)
    suspend fun getMovieCategories(storedLanguage: String): List<LocalMovieCategoryDto>
    suspend fun upsertTvShowCategories(categories: List<LocalTvShowCategoryDto>)
    suspend fun getTvShowCategories(storedLanguage: String): List<LocalTvShowCategoryDto>
}
