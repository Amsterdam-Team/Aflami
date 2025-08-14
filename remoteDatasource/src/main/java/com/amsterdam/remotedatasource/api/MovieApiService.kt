package com.amsterdam.remotedatasource.api

import com.amsterdam.remotedatasource.utils.RequiresSessionId
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.ActorSearchRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): MovieRemoteResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): MovieRemoteResponse

    @GET("search/movie")
    suspend fun getMoviesByKeyword(
        @Query("query") keyword: String,
        @Query("page") page: Int
    ): MovieRemoteResponse

    @GET("search/person")
    suspend fun getActorIdByName(
        @Query("query") name: String,
        @Query("page") page: Int
    ): ActorSearchRemoteResponse

    @GET("discover/movie")
    suspend fun getMoviesByActorId(
        @Query("with_cast") actorIds: String
    ): MovieRemoteResponse

    @GET("discover/movie")
    suspend fun getMoviesByCountryIsoCode(
        @Query("with_origin_country") countryIsoCode: String,
        @Query("page") page: Int
    ): MovieRemoteResponse

    @GET("movie/{movieId}/credits")
    suspend fun getCastByMovieId(
        @Path("movieId") movieId: Long
    ): CastAndCrewRemoteResponse

    @GET("movie/{movieId}")
    @RequiresSessionId
    suspend fun getMovieDetailsById(
        @Path("movieId") movieId: Long,
        @Query("append_to_response") append: String = "reviews,credits,actors,similar,images,videos,account_states",
        @Query("include_video_language") videoLang: String = "en"
    ): MovieDetailsRemoteResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenreIds(
        @Query("with_genres") genresIds: List<Long>,
        @Query("page") page: Int
    ): MovieRemoteResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): MovieRemoteResponse

    @RequiresSessionId
    @FormUrlEncoded
    @POST("movie/{movie_id}/rating")
    suspend fun postMovieRating(
        @Path("movie_id") movieId: Long,
        @Field("value") rate: Float,
    ): RatingRemoteResponse

    @RequiresSessionId
    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int = 0,
    ): MovieRemoteResponse

    @RequiresSessionId
    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteMovieRate(
        @Path("movie_id") movieId: Long,
    )
}