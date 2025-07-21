package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryRemoteDataSourceImpl(
    private val networkClient: NetworkClient
) : CategoryRemoteSource {
    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return responseCall { networkClient.get(GET_MOVIE_GENRE_LIST) }
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return responseCall { networkClient.get(GET_TV_SHOW_GENRE_LIST) }
    }

    private companion object {
        const val GET_MOVIE_GENRE_LIST = "genre/movie/list"
        const val GET_TV_SHOW_GENRE_LIST = "genre/tv/list"
    }
}