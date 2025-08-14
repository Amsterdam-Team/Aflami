package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.CategoryRemoteDataSource
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import javax.inject.Inject

class CategoryRemoteDataDataSourceImpl @Inject constructor(
    private val categoryApiService: CategoryApiService
) : CategoryRemoteDataSource {

    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getMovieCategories() }
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getTvShowCategories() }
    }
}