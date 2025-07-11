package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.Constant.BASE_URL
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.call.body

class RemoteMovieDatasourceImpl(private val ktorClient: KtorClient) : RemoteMovieDatasource {

    override suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        rating: Int,
        categoryId: Long?
    ): List<RemoteMovieResponse> {
        val selectedCategoryId: String = categoryId?.toString() ?: ""
        return ktorClient.get("$BASE_URL/discover/movie&query=$keyword&vote_average.lte=$rating&with_genres=$selectedCategoryId")
            .body()
    }


}
