package com.example.remotedatasource.api

import com.example.repository.dto.remote.RemoteTvShowResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TvShowsApiService {

    @GET(SEARCH_TV_URL)
    suspend fun getTvShowsByKeyword(
        @Query(QUERY_KEY) keyword: String,
        @Query(PAGE_KEY) page: Int
    ): RemoteTvShowResponse

    companion object {
        private const val SEARCH_TV_URL = "search/tv"
        private const val QUERY_KEY = "query"
        private const val PAGE_KEY = "page"
    }
}