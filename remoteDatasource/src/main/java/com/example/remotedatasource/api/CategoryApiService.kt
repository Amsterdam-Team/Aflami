package com.example.remotedatasource.api

import com.example.repository.dto.remote.RemoteCategoryResponse
import retrofit2.http.GET

interface CategoryApiService {
    @GET(MOVIE_GENRE_LIST)
    suspend fun getMovieCategories(): RemoteCategoryResponse

    @GET(TV_SHOW_GENRE_LIST)
    suspend fun getTvShowCategories(): RemoteCategoryResponse

    companion object {
        private const val MOVIE_GENRE_LIST = "genre/movie/list"
        private const val TV_SHOW_GENRE_LIST = "genre/tv/list"
    }
}