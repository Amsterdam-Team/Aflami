package com.example.remotedatasource.serviceProvider

import com.example.repository.dto.remote.RemoteCategoryResponse

interface CategoryServiceProvider {
    suspend fun getMovieCategories(): RemoteCategoryResponse
    suspend fun getTvShowCategories(): RemoteCategoryResponse
}