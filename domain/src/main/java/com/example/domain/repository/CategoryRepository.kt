package com.example.domain.repository

import com.example.entity.Category

interface CategoryRepository {

    suspend fun getMovieCategories(): List<Category>
    suspend fun getTvShowCategories(): List<Category>
    suspend fun getMovieCategories(movieId : Long) : List<Category>
}