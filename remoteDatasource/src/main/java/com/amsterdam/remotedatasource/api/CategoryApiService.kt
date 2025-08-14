package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import retrofit2.http.GET

interface CategoryApiService {
    @GET("genre/movie/list")
    suspend fun getMovieCategories(): CategoryRemoteResponse

    @GET("genre/tv/list")
    suspend fun getTvShowCategories(): CategoryRemoteResponse

}