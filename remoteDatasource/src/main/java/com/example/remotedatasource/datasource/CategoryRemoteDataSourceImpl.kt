package com.example.remotedatasource.datasource

import com.example.remotedatasource.api.CategoryApiService
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryRemoteDataSourceImpl(
    private val categoryApiService: CategoryApiService
) : CategoryRemoteSource {
    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getMovieCategories() }
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getTvShowCategories() }
    }
}