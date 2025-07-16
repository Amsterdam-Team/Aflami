package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryRemoteSourceImpl(
    private val ktorClient: KtorClient
) : CategoryRemoteSource {
    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return ktorClient.tryToExecute { ktorClient.get(GET_MOVIE_GENRE_LIST) }
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return ktorClient.tryToExecute { ktorClient.get(GET_TV_SHOW_GENRE_LIST) }
    }

    private companion object {
        const val GET_MOVIE_GENRE_LIST = "genre/movie/list"
        const val GET_TV_SHOW_GENRE_LIST = "genre/tv/list"
    }
}