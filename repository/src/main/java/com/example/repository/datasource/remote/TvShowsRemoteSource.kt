package com.example.repository.datasource.remote

import com.example.repository.dto.remote.EpisodeResponse
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse

interface TvShowsRemoteSource {
    suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse

    suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse

    suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse

    suspend fun getSimilarTvShows(tvShowId: Long): RemoteTvShowResponse

    suspend fun getTvShowReviews(tvShowId: Long): ReviewsResponse

    suspend fun getTvShowGallery(tvShowId: Long): RemoteGalleryResponse

    suspend fun getTvShowCompanyProduction(tvShowId: Long): ProductionCompanyResponse

    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): EpisodeResponse
}

