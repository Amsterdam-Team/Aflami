package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

interface TvShowsRemoteSource {

    suspend fun getPopularTvShows(): RemoteTvShowResponse
    suspend fun getTopRatedTvShows(): RemoteTvShowResponse

    suspend fun getTvShowsByKeyword(
        keyword: String,
        page: Int,
    ): RemoteTvShowResponse

    suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse

    suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse

    suspend fun getSimilarTvShows(tvShowId: Long): RemoteTvShowResponse

    suspend fun getTvShowReviews(tvShowId: Long): ReviewsResponse

    suspend fun getTvShowGallery(tvShowId: Long): RemoteGalleryResponse

    suspend fun getTvShowCompanyProduction(tvShowId: Long): ProductionCompanyResponse

    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): EpisodeResponse
}
