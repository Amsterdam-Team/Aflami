package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteCategoryResponse

interface RemoteCategoryDatasource {
    suspend fun getMovieCategories(): RemoteCategoryResponse

    suspend fun getTvShowCategories(): RemoteCategoryResponse
}