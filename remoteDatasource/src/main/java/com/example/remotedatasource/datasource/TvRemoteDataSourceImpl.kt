package com.example.remotedatasource.datasource

import com.example.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse

class TvRemoteDataSourceImpl(
    private val tvShowsServiceProvider: TvShowsServiceProvider
) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return tvShowsServiceProvider.getTvShowsByKeyword(keyword, page)
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return responseCall {
            networkClient.get("tv/$tvShowId")
        }
    }

    override suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse {
        return responseCall {
            networkClient.get("tv/$tvShowId/credits")
        }
    }

    override suspend fun getSimilarTvShows(tvShowId: Long): RemoteTvShowResponse {
        return responseCall {
            networkClient.get("tv/$tvShowId/similar")
        }
    }

    override suspend fun getTvShowReviews(tvShowId: Long): ReviewsResponse {
        return responseCall {
            networkClient.get("tv/$tvShowId/reviews")
        }
    }

    override suspend fun getTvShowGallery(tvShowId: Long): RemoteGalleryResponse {
        return responseCall {
            networkClient.get("tv/$tvShowId/images")
        }
    }

    override suspend fun getTvShowCompanyProduction(tvShowId: Long): ProductionCompanyResponse {
        return responseCall {
            networkClient.get("tv/$tvShowId")
        }
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return responseCall {
            networkClient.get("tv/$tvShowId/season/$seasonNumber")
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val PAGE = "page"
        const val SEARCH_TV_URL = "search/tv"
    }
}
