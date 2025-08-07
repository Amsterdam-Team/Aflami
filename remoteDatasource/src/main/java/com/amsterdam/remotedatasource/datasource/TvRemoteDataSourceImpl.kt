package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoResponse
import javax.inject.Inject

class TvRemoteDataSourceImpl @Inject constructor(
    private val tvShowsApiService: TvShowsApiService
) : TvShowsRemoteSource {
    override suspend fun getPopularTvShows(): RemoteTvShowResponse {
        return responseCall { tvShowsApiService.getPopularTvShows() }
    }

    override suspend fun getTopRatedTvShows(
        page: Int
    ): RemoteTvShowResponse {
        return responseCall { tvShowsApiService.getTopRatedTvShows(page) }
    }

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return responseCall {
            tvShowsApiService.getTvShowsByKeyword(keyword, page)
        }
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long, sessionId: String): TvShowDetailsRemoteResponse {
        return responseCall { tvShowsApiService.getTvShowDetailsById(tvShowId, sessionId) }
    }

    override suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse {
        return responseCall { tvShowsApiService.getTvShowCast(tvShowId) }
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeResponse {
        return responseCall {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        }
    }

    override suspend fun getRatedTvShows(sessionId: String): RemoteTvShowResponse {
        return responseCall { tvShowsApiService.getRatedTvShows(sessionId = sessionId) }
    }

    override suspend fun setTvShowRate(
        rate: Int,
        tvShowId: Long,
        sessionId: String
    ): RatingResponse? {
        return responseCall {
            tvShowsApiService.postTvRating(
                tvId = tvShowId,
                rate = rate.toFloat(),
                sessionId = sessionId
            )
        }
    }

    override suspend fun deleteTvShowRate(tvShowId: Long, sessionId: String) {
        responseCall { tvShowsApiService.deleteTvRating(tvId = tvShowId, sessionId = sessionId) }
    }

    override suspend fun getEpisodeVideos(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): VideoResponse {
        val actualSeasonNumber = if (seasonNumber<=0) 1 else seasonNumber
        return  responseCall {
            tvShowsApiService.getEpisodeVideos(
                tvShowId,
                actualSeasonNumber,
                episodeNumber
            )
        }
    }
}