package com.amsterdam.remotedatasource.api

import com.amsterdam.remotedatasource.utils.RequiresSessionId
import com.amsterdam.repository.dto.remote.EpisodeRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApiService {

    @GET("tv/popular")
    suspend fun getPopularTvShows(): TvShowRemoteResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int
    ): TvShowRemoteResponse

    @GET("search/tv")
    suspend fun getTvShowsByKeyword(
        @Query("query") keyword: String,
        @Query("page") page: Int
    ): TvShowRemoteResponse

    @GET("tv/{tvShowId}/credits")
    suspend fun getTvShowCast(
        @Path("tvShowId") tvShowId: Long
    ): CastAndCrewRemoteResponse

    @RequiresSessionId
    @GET("tv/{tvShowId}")
    suspend fun getTvShowDetailsById(
        @Path("tvShowId") tvShowId: Long,
        @Query("append_to_response") appendToResponse: String = "credits,similar,reviews,images,videos,account_states",
        @Query("include_video_language") videoLang: String = "en"
    ): TvShowDetailsRemoteResponse

    @GET("tv/{tvShowId}/season/{seasonNumber},")
    suspend fun getEpisodesBySeasonNumber(
        @Path("tvShowId") tvShowId: Long,
        @Path("seasonNumber") seasonNumber: Int
    ): EpisodeRemoteResponse

    @GET("discover/tv")
    suspend fun getTvShowsByGenreIds(
        @Query("with_genres") genresIds: List<Long>,
        @Query("page") page: Int
    ): TvShowRemoteResponse

    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}/videos")
    suspend fun getEpisodeVideos(
        @Path("series_id") tvShowId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("include_video_language") videoLang: String = "en"
    ): VideoRemoteResponse


    @RequiresSessionId
    @FormUrlEncoded
    @POST("tv/{tv_id}/rating")
    suspend fun postTvRating(
        @Path("tv_id") tvId: Long,
        @Field("value") rate: Float,
    ): RatingRemoteResponse

    @RequiresSessionId
    @DELETE("tv/{tv_id}/rating")
    suspend fun deleteTvRating(
        @Path("tv_id") tvId: Long,
    ): RatingRemoteResponse

    @RequiresSessionId
    @GET("account/{account_id}/rated/tv")
    suspend fun getRatedTvShows(
        @Path("account_id") accountId: Int = 0,
    ): TvShowRemoteResponse
}