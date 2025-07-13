package com.amsterdam.domain.repository

import com.amsterdam.entity.Category

interface CategoryRepository {
    suspend fun getMovieCategories(): List<Category>

    suspend fun getTvShowCategories(): List<Category>
}