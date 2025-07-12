package com.example.remotedatasource.client

import com.example.remotedatasource.BuildConfig

object Endpoints {
    private  val BASE_URL = BuildConfig.BASE_URL
     val GET_MOVIES_BY_ACTOR_NAME_URL = "$BASE_URL/discover/movie"
     val GET_ACTOR_NAME_BY_ID_URL = "$BASE_URL/search/person"
}