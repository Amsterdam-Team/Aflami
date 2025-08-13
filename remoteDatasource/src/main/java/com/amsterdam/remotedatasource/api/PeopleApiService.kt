package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.RemotePeopleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleApiService {

    @GET("trending/person/day")
    suspend fun getTrendingPeople(
        @Query("page") page: Int = 1
    ): RemotePeopleResponse
}