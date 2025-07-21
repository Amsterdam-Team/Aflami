package com.example.remotedatasource.datasource

import com.example.remotedatasource.serviceProvider.CategoryServiceProvider
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryRemoteDataSourceImpl(
    private val categoryServiceProvider: CategoryServiceProvider
) : CategoryRemoteSource {

    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return categoryServiceProvider.getMovieCategories()
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return categoryServiceProvider.getTvShowCategories()
    }
}