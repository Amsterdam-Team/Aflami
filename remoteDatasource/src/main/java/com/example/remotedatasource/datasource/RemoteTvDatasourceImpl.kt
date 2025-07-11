package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.Constant.BASE_URL
import com.example.repository.datasource.remote.RemoteTvShowsDatasource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.call.body

class RemoteTvDatasourceImpl(private val ktorClient: KtorClient) : RemoteTvShowsDatasource {

    override suspend fun getTvShowsByKeyword(
        keyword: String,
        rating: Int,
        categoryId: Long?
    ): List<RemoteTvShowResponse> {
        val selectedCategoryId: String = categoryId?.toString() ?: ""
        return ktorClient.get("$BASE_URL/discover/tv&query=$keyword&vote_average.lte=$rating&with_genres=$selectedCategoryId")
            .body()
    }
}