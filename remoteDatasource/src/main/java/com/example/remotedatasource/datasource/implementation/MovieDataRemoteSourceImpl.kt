package com.example.remotedatasource.datasource.implementation

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.getActorNameByIdUrl
import com.example.remotedatasource.client.getMoviesByActorNameUrl
import com.example.remotedatasource.client.safeCall
import com.example.remotedatasource.datasource.dto.ActorSearchResponseDto
import com.example.remotedatasource.datasource.dto.MovieSearchResponseDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDataRemoteSourceImpl(
    private val client: KtorClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private suspend fun getActorIdByName(name: String, language: String): ActorSearchResponseDto {
        return withContext(dispatcher) {
            safeCall<ActorSearchResponseDto> {
                client.httpClient.get {
                    getActorNameByIdUrl()
                    parameter(LANGUAGE_KEY, language)
                    parameter(QUERY_KEY, name)
                }
            }
        }
    }

    suspend fun getMoviesByActorName(name: String, language: String): MovieSearchResponseDto {
        return withContext(dispatcher) {
            safeCall<MovieSearchResponseDto> {
                val actorsByName = getActorIdByName(name, language).actors
                    .joinToString(separator = "|") { it.name }

                client.httpClient.get {
                    getMoviesByActorNameUrl()
                    parameter(LANGUAGE_KEY, language)
                    parameter(WITH_CAST_KEY, actorsByName)
                }
            }
        }
    }

    private companion object {
        const val LANGUAGE_KEY = "language"
        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"
    }
}