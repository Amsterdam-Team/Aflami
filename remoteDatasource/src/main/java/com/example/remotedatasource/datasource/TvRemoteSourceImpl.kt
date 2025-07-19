package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.dto.remote.SeasonResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
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

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteDto {
        return safeCall {
            val response = ktorClient.get("tv/$tvShowId")
            return json.decodeFromString<TvShowDetailsRemoteDto>(response.bodyAsText())
        }
    }

    override suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse {
        return safeCall {
            val response = ktorClient.get("tv/$tvShowId/credits")
            return json.decodeFromString<RemoteCastAndCrewResponse>(response.bodyAsText())
        }
    }

    override suspend fun getSimilarTvShows(tvShowId: Long): RemoteTvShowResponse {
        return safeCall {
            val response = ktorClient.get("tv/$tvShowId/similar")
            return json.decodeFromString<RemoteTvShowResponse>(response.bodyAsText())
        }
    }

    override suspend fun getTvShowReviews(tvShowId: Long): ReviewsResponse {
        return safeCall {
            val response = ktorClient.get("tv/$tvShowId/reviews")
            return json.decodeFromString<ReviewsResponse>(response.bodyAsText())
        }
    }

    override suspend fun getTvShowGallery(tvShowId: Long): RemoteGalleryResponse {
        return safeCall {
            val response = ktorClient.get("tv/$tvShowId/images")
            return json.decodeFromString<RemoteGalleryResponse>(response.bodyAsText())
        }
    }

    override suspend fun getTvShowCompanyProduction(tvShowId: Long): ProductionCompanyResponse {
        return safeCall {
            val response = ktorClient.get("tv/$tvShowId")
            return json.decodeFromString<ProductionCompanyResponse>(response.bodyAsText())
        }
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return safeCall {
            val response = ktorClient.get("tv/$tvShowId/season/$seasonNumber")
            return json.decodeFromString<EpisodeResponse>(response.bodyAsText())
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_TV_URL = "search/tv"
    }
}
