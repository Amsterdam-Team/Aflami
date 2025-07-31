package com.amsterdam.remotedatasource.serviceProvider

import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse

interface TvShowsServiceProvider {
    suspend fun getPopularTvShows(): RemoteTvShowResponse
    suspend fun getTopRatedTvShows(): RemoteTvShowResponse
    suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse
    suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse
    suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse
    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): EpisodeResponse
}