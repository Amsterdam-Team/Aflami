package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApiService {

    @GET("tv/popular")
    suspend fun getPopularTvShows(): RemoteTvShowResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int
    ): RemoteTvShowResponse

    @GET("search/tv")
    suspend fun getTvShowsByKeyword(
        @Query("query") keyword: String,
        @Query("page") page: Int
    ): RemoteTvShowResponse

    @GET("tv/{tvShowId}/credits")
    suspend fun getTvShowCast(
        @Path("tvShowId") tvShowId: Long
    ): RemoteCastAndCrewResponse

    @GET("tv/{tvShowId}")
    suspend fun getTvShowDetailsById(
        @Path("tvShowId") tvShowId: Long,
        @Query("session_id") sessionId: String = "",
        @Query("append_to_response") appendToResponse: String = "credits,similar,reviews,images,videos,account_states",
        @Query("include_video_language") videoLang: String = "en"
    ): TvShowDetailsRemoteResponse

    @GET("tv/{tvShowId}/season/{seasonNumber},")
    suspend fun getEpisodesBySeasonNumber(
        @Path("tvShowId") tvShowId: Long,
        @Path("seasonNumber") seasonNumber: Int
    ): EpisodeResponse

    @GET("discover/tv")
    suspend fun getTvShowsByGenreIds(
        @Query("with_genres") genresIds: List<Long>,
        @Query("page") page: Int
    ): RemoteTvShowResponse

    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}/videos")
    suspend fun getEpisodeVideos(
        @Path("series_id") tvShowId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("include_video_language") videoLang: String = "en"
    ): VideoResponse


    @FormUrlEncoded
    @POST("tv/{tv_id}/rating")
    suspend fun postTvRating(
        @Path("tv_id") tvId: Long,
        @Field("value") rate: Float,
        @Query("session_id") sessionId: String
    ): RatingResponse

    @DELETE("tv/{tv_id}/rating")
    suspend fun deleteTvRating(
        @Path("tv_id") tvId: Long,
        @Query("session_id") sessionId: String
    ): RatingResponse

    @GET("account/{account_id}/rated/tv")
    suspend fun getRatedTvShows(
        @Path("account_id") accountId: Int = 0,
        @Query("session_id") sessionId: String
    ): RemoteTvShowResponse
}