package com.example.remotedatasource.client

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url

private const val BaseUrl = "https://api.themoviedb.org/3"
fun HttpRequestBuilder.getMoviesByActorNameUrl() = url("$BaseUrl/discover/movie")
fun HttpRequestBuilder.getActorNameByIdUrl() = url("$BaseUrl/search/person")