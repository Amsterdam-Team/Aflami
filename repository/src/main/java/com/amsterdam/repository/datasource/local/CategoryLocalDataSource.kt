package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto

interface CategoryLocalDataSource {
    suspend fun upsertMovieCategories(categories: List<MovieCategoryLocalDto>)
    suspend fun getMovieCategories(): List<MovieCategoryLocalDto>
    suspend fun upsertTvShowCategories(categories: List<TvShowCategoryLocalDto>)
    suspend fun getTvShowCategories(): List<TvShowCategoryLocalDto>
}
