package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.Constant.BASE_URL
import com.example.repository.datasource.remote.RemoteCategoryDatasource
import com.example.repository.dto.remote.RemoteCategoryResponse
import io.ktor.client.call.body

class RemoteCategoryDatasourceImpl(
    private val ktorClient: KtorClient
) : RemoteCategoryDatasource {
    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return ktorClient.get("$BASE_URL/genre/movie/list")
            .body()
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return ktorClient.get("$BASE_URL/genre/tv/list")
            .body()
    }
}