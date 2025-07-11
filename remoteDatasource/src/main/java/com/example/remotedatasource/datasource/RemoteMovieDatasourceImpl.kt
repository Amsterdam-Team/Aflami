package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.Endpoints
import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.safeCall
import com.example.remotedatasource.utils.Constant.BASE_URL
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.call.body

class RemoteMovieDatasourceImpl(private val ktorClient: KtorClient) : RemoteMovieDatasource {

    override suspend fun getMoviesByKeyword(
        keyword: String,
        rating: Int,
        categoryId: Long?
    ): List<RemoteMovieResponse> {
        val selectedCategoryId: String = categoryId?.toString() ?: ""
        return ktorClient.get("$BASE_URL/discover/movie&query=$keyword&vote_average.lte=$rating&with_genres=$selectedCategoryId")
            .body()
    }

    override suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val actorsByName = getActorIdByName(name)
                .actors
                .joinToString(separator = "|") { it.name }

            ktorClient.get("${Endpoints.GET_MOVIES_BY_ACTOR_NAME_URL}?$WITH_CAST_KEY=$actorsByName")
        }
    }

    private suspend fun getActorIdByName(
        name: String
    ): RemoteActorSearchResponse {
        return safeCall<RemoteActorSearchResponse> {
            ktorClient.get("${Endpoints.GET_ACTOR_NAME_BY_ID_URL}?$QUERY_KEY=$name")
        }
    }

    private companion object {
        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"
    }
}