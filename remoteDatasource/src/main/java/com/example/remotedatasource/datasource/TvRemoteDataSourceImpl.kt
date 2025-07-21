package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.EpisodeResponse
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.ktor.client.request.parameter

class TvRemoteDataSourceImpl(
    private val networkClient: NetworkClient
) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return responseCall {
            networkClient.get(SEARCH_TV_URL) {
                parameter(QUERY_KEY, keyword)
                parameter(PAGE, page)
            }
        }
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
