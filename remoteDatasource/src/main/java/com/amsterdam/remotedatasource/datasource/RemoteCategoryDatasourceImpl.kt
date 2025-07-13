package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.client.KtorClient
import com.amsterdam.repository.datasource.remote.RemoteCategoryDatasource
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import io.ktor.client.call.body

class RemoteCategoryDatasourceImpl(
    private val ktorClient: KtorClient
) : RemoteCategoryDatasource {
    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return ktorClient.get(GET_MOVIE_GENRE_LIST).body()
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return ktorClient.get(GET_TV_SHOW_GENRE_LIST).body()
    }

    private companion object {
        const val GET_MOVIE_GENRE_LIST = "genre/movie/list"

        const val GET_TV_SHOW_GENRE_LIST = "discover/tv"
    }
}