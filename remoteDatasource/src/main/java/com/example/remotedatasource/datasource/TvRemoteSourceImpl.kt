package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.EpisodeResponse
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json

class TvRemoteSourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json
) :
    TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse {
        return ktorClient.tryToExecute {
            ktorClient.get(SEARCH_TV_URL) { parameter(QUERY_KEY, keyword) }
        }
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return ktorClient.tryToExecute {
            ktorClient.get("tv/$tvShowId")
        }
    }

    override suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse {
        return ktorClient.tryToExecute {
            ktorClient.get("tv/$tvShowId/credits")
        }
    }

    override suspend fun getSimilarTvShows(tvShowId: Long): RemoteTvShowResponse {
        return ktorClient.tryToExecute {
            ktorClient.get("tv/$tvShowId/similar")
        }
    }

    override suspend fun getTvShowReviews(tvShowId: Long): ReviewsResponse {
        return ktorClient.tryToExecute {
            ktorClient.get("tv/$tvShowId/reviews")
        }
    }

    override suspend fun getTvShowGallery(tvShowId: Long): RemoteGalleryResponse {
        return ktorClient.tryToExecute {
            ktorClient.get("tv/$tvShowId/images")
        }
    }

    override suspend fun getTvShowCompanyProduction(tvShowId: Long): ProductionCompanyResponse {
        return ktorClient.tryToExecute {
            ktorClient.get("tv/$tvShowId")
        }
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return ktorClient.tryToExecute {
            ktorClient.get("tv/$tvShowId/season/$seasonNumber")
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_TV_URL = "search/tv"
    }
}
