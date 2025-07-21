package com.example.remotedatasource.serviceProvider.implementation

import com.example.remotedatasource.api.CategoryApiService
import com.example.remotedatasource.serviceProvider.CategoryServiceProvider
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryServiceProviderImpl(
    private val categoryApiService: CategoryApiService
) : CategoryServiceProvider {
    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getMovieCategories() }
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getTvShowCategories() }
    }
}