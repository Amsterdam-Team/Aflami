package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.RemoteCharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApiService {

    @GET("trending/person/day")
    suspend fun getTrendingCharacters(
        @Query("page") page: Int = 1
    ): RemoteCharacterResponse
}