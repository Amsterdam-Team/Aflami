package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApiService {

    @GET(TV_POPULAR)
    suspend fun getPopularTvShows(): RemoteTvShowResponse

    @GET(TOP_RATED_TV_SHOWS)
    suspend fun getTopRatedTvShows(): RemoteTvShowResponse

    @GET(SEARCH_TV_URL)
    suspend fun getTvShowsByKeyword(
        @Query(QUERY_KEY) keyword: String,
        @Query(PAGE_KEY) page: Int
    ): RemoteTvShowResponse

    @GET(TV_SHOW_DETAILS_ENDPOINT)
    suspend fun getTvShowDetailsById(
        @Path("tvShowId") tvShowId: Long
    ): TvShowDetailsRemoteResponse

    @GET(TV_SHOW_CREDITS_ENDPOINT)
    suspend fun getTvShowCast(
        @Path("tvShowId") tvShowId: Long
    ): RemoteCastAndCrewResponse

    @GET(TV_SHOW_SIMILAR_ENDPOINT)
    suspend fun getSimilarTvShows(
        @Path("tvShowId") tvShowId: Long
    ): RemoteTvShowResponse

    @GET(TV_SHOW_REVIEWS_ENDPOINT)
    suspend fun getTvShowReviews(
        @Path("tvShowId") tvShowId: Long
    ): ReviewsResponse

    @GET(TV_SHOW_IMAGES_ENDPOINT)
    suspend fun getTvShowGallery(
        @Path("tvShowId") tvShowId: Long
    ): RemoteGalleryResponse

    @GET(TV_SHOW_DETAILS_ENDPOINT)
    suspend fun getProductionCompany(
        @Path("tvShowId") tvShowId: Long
    ): ProductionCompanyResponse

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
        private const val TV_SHOW_DETAILS_ENDPOINT = "tv/{tvShowId}"
        private const val TV_SHOW_CREDITS_ENDPOINT = "tv/{tvShowId}/credits"
        private const val TV_SHOW_SIMILAR_ENDPOINT = "tv/{tvShowId}/similar"
        private const val TV_SHOW_REVIEWS_ENDPOINT = "tv/{tvShowId}/reviews"
        private const val TV_SHOW_IMAGES_ENDPOINT = "tv/{tvShowId}/images"
        private const val TV_SHOW_EPISODES_ENDPOINT = "tv/{tvShowId}/season/{seasonNumber}"
    }
}