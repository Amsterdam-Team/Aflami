package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.dto.remote.EpisodeRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import javax.inject.Inject

class TvRemoteDataSourceImpl @Inject constructor(
    private val tvShowsApiService: TvShowsApiService
) : TvShowsRemoteDataSource {
    override suspend fun getPopularTvShows(): TvShowRemoteResponse {
        return responseCall { tvShowsApiService.getPopularTvShows() }
    }

    override suspend fun getTopRatedTvShows(
        page: Int
    ): TvShowRemoteResponse {
        return responseCall { tvShowsApiService.getTopRatedTvShows(page) }
    }

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): TvShowRemoteResponse {
        return responseCall {
            tvShowsApiService.getTvShowsByKeyword(keyword, page)
        }
    }

    override suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse {
        return responseCall { tvShowsApiService.getTvShowDetailsById(tvShowId) }
    }

    override suspend fun getTvShowCast(tvShowId: Long): CastAndCrewRemoteResponse {
        return responseCall { tvShowsApiService.getTvShowCast(tvShowId) }
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): EpisodeRemoteResponse {
        return responseCall {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        }
    }

    override suspend fun getRatedTvShows(): TvShowRemoteResponse {
        return responseCall { tvShowsApiService.getRatedTvShows() }
    }

    override suspend fun setTvShowRate(
        rate: Int,
        tvShowId: Long,
    ): RatingRemoteResponse? {
        return responseCall {
            tvShowsApiService.postTvRating(
                tvId = tvShowId,
                rate = rate.toFloat(),
            )
        }
    }

    override suspend fun deleteTvShowRate(tvShowId: Long) {
        responseCall { tvShowsApiService.deleteTvRating(tvId = tvShowId) }
    }

    override suspend fun getEpisodeVideos(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): VideoRemoteResponse {
        val actualSeasonNumber = if (seasonNumber <= 0) 1 else seasonNumber
        return responseCall {
            tvShowsApiService.getEpisodeVideos(
                tvShowId,
                actualSeasonNumber,
                episodeNumber
            )
        }
    }

    override suspend fun getTvShowsByGenreId(
        genreId: Long,
        page: Int
    ): TvShowRemoteResponse {
        return responseCall { tvShowsApiService.getTvShowsByGenreIds(listOf(genreId), page) }
    }
}