package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
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

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return responseCall { tvShowsApiService.getTvShowDetailsById(tvShowId) }
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
}
