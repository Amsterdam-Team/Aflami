package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApiService {

    @GET(TV_POPULAR)
    suspend fun getPopularTvShows(): RemoteTvShowResponse

    @GET(TOP_RATED_TV_SHOWS)
    suspend fun getTopRatedTvShows(
        @Query(PAGE_KEY) page: Int
    ): RemoteTvShowResponse

    @GET(SEARCH_TV_URL)
    suspend fun getTvShowsByKeyword(
        @Query(QUERY_KEY) keyword: String,
        @Query(PAGE_KEY) page: Int
    ): RemoteTvShowResponse

    @GET(TV_SHOW_CREDITS_ENDPOINT)
    suspend fun getTvShowCast(
        @Path("tvShowId") tvShowId: Long
    ): RemoteCastAndCrewResponse

    @GET(TV_SHOW_DETAILS_ENDPOINT)
    suspend fun getTvShowDetailsById(
        @Path("tvShowId") tvShowId: Long,
        @Query("append_to_response") appendToResponse: String = TV_SHOW_DETAILS_APPEND_PARAMETERS,
        @Query("include_video_language") videoLang: String = INCLUDED_VIDEO_LANGUAGE
    ): TvShowDetailsRemoteResponse

    @GET(TV_SHOW_EPISODES_ENDPOINT)
    suspend fun getEpisodesBySeasonNumber(
        @Path("tvShowId") tvShowId: Long,
        @Path("seasonNumber") seasonNumber: Int
    ): EpisodeResponse

    companion object {

        private const val TV_POPULAR = "tv/popular"

        private const val TOP_RATED_TV_SHOWS = "tv/top_rated"

        private const val SEARCH_TV_URL = "search/tv"
        private const val QUERY_KEY = "query"
        private const val PAGE_KEY = "page"
        private const val TV_SHOW_CREDITS_ENDPOINT = "tv/{tvShowId}/credits"
        private const val TV_SHOW_DETAILS_ENDPOINT = "tv/{tvShowId}"
        private const val TV_SHOW_DETAILS_APPEND_PARAMETERS = "credits,similar,reviews,images,videos"
        private const val TV_SHOW_EPISODES_ENDPOINT = "tv/{tvShowId}/season/{seasonNumber}"
        private const val INCLUDED_VIDEO_LANGUAGE = "en"

    }
}