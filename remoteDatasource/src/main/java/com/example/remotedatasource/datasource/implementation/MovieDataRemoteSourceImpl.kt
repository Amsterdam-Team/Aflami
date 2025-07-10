package com.example.remotedatasource.datasource.implementation

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.getActorNameByIdUrl
import com.example.remotedatasource.client.getMoviesByActorNameUrl
import com.example.remotedatasource.datasource.dto.ActorSearchResponseDto
import com.example.remotedatasource.datasource.dto.MovieSearchResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDataRemoteSourceImpl(
    private val client: KtorClient
) {

    private suspend fun getActorIdByName(name: String): ActorSearchResponseDto {
        return withContext(Dispatchers.IO) {
            client.httpClient.get {
                getActorNameByIdUrl()
                parameter(LANGUAGE_KEY, "en")
                parameter(QUERY_KEY, name)
            }.body()
        }
    }

    suspend fun getMoviesByActorName(name: String): MovieSearchResponseDto {
        return withContext(Dispatchers.IO) {
            val actorsByName = getActorIdByName(name).actors
                .joinToString(separator = "|") { it.name }

            client.httpClient.get {
                getMoviesByActorNameUrl()
                parameter(LANGUAGE_KEY, "en")
                parameter(WITH_CAST_KEY, actorsByName)
            }.body()
        }
    }

    private companion object {
        const val LANGUAGE_KEY = "language"
        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"
    }
}