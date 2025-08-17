package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.EpisodeRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse

interface TvShowsRemoteDataSource {
    suspend fun getPopularTvShows(): TvShowRemoteResponse

    suspend fun getTopRatedTvShows(page: Int): TvShowRemoteResponse

    suspend fun getTvShowCast(tvShowId: Long): CastAndCrewRemoteResponse

    suspend fun getTvShowsByKeyword(keyword: String, page: Int, ): TvShowRemoteResponse

    suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse

    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): EpisodeRemoteResponse

    suspend fun getEpisodeVideos(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): VideoRemoteResponse

    suspend fun getRatedTvShows(): TvShowRemoteResponse

    suspend fun setTvShowRate(rate: Int, tvShowId: Long): RatingRemoteResponse?

    suspend fun getTvShowsByGenreId(genreId: Long, page: Int): TvShowRemoteResponse

    suspend fun deleteTvShowRate(tvShowId: Long)
}