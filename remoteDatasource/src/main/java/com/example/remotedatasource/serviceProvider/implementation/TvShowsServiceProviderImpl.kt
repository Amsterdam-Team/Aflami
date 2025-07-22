package com.example.remotedatasource.serviceProvider.implementation

import com.example.remotedatasource.api.TvShowsApiService
import com.example.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.dto.remote.EpisodeResponse
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse

class TvShowsServiceProviderImpl(
    private val tvShowsApiService: TvShowsApiService
) : TvShowsServiceProvider {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return responseCall {
            tvShowsApiService.getTvShowsByKeyword(keyword, page)
        }
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return responseCall { tvShowsApiService.getTvShowDetailsById(tvShowId) }
    }

    override suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse {
        return responseCall { tvShowsApiService.getTvShowCast(tvShowId) }
    }

    override suspend fun getSimilarTvShows(tvShowId: Long): RemoteTvShowResponse {
        return tvShowsApiService.getSimilarTvShows(tvShowId)
    }

    override suspend fun getTvShowReviews(tvShowId: Long): ReviewsResponse {
        return tvShowsApiService.getTvShowReviews(tvShowId)
    }

    override suspend fun getTvShowGallery(tvShowId: Long): RemoteGalleryResponse {
        return tvShowsApiService.getTvShowGallery(tvShowId)
    }

    override suspend fun getTvShowCompanyProduction(tvShowId: Long): ProductionCompanyResponse {
        return tvShowsApiService.getProductionCompany(tvShowId)
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return tvShowsApiService.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
    }
}