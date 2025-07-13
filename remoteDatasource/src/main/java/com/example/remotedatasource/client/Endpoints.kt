package com.example.remotedatasource.client

import com.example.remotedatasource.BuildConfig

object Endpoints {
    private const val BASE_URL = BuildConfig.BASE_URL
    const val SEARCH_MOVIE_URL = "$BASE_URL/search/movie"
    const val GET_ACTOR_NAME_BY_ID_URL = "$BASE_URL/search/person"

    const val DISCOVER_MOVIE = "$BASE_URL/discover/movie"
}