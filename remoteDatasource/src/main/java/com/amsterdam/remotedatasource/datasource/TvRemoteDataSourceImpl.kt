package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse

class TvRemoteDataSourceImpl(
    private val tvShowsServiceProvider: TvShowsServiceProvider
) : TvShowsRemoteSource {
    override suspend fun getPopularTvShows(): RemoteTvShowResponse {
        return tvShowsServiceProvider.getPopularTvShows()
    }

    override suspend fun getTopRatedTvShows(): RemoteTvShowResponse {
        return tvShowsServiceProvider.getTopRatedTvShows()
    }

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return tvShowsServiceProvider.getTvShowsByKeyword(keyword, page)
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return tvShowsServiceProvider.getTvShowDetailsById(tvShowId)
    }

    override suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse {
        return tvShowsServiceProvider.getTvShowCast(tvShowId)
    }

    override suspend fun getSimilarTvShows(tvShowId: Long): RemoteTvShowResponse {
        return tvShowsServiceProvider.getSimilarTvShows(tvShowId)
    }

    override suspend fun getTvShowReviews(tvShowId: Long): ReviewsResponse {
        return tvShowsServiceProvider.getTvShowReviews(tvShowId)
    }

    override suspend fun getTvShowGallery(tvShowId: Long): RemoteGalleryResponse {
        return tvShowsServiceProvider.getTvShowGallery(tvShowId)
    }

    override suspend fun getTvShowCompanyProduction(tvShowId: Long): ProductionCompanyResponse {
        return tvShowsServiceProvider.getTvShowCompanyProduction(tvShowId)
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return tvShowsServiceProvider.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
    }
}
