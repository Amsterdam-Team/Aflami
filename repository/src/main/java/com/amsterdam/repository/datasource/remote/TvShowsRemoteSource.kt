package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.VideoResponse

interface TvShowsRemoteSource {

    suspend fun getPopularTvShows(): RemoteTvShowResponse
    suspend fun getTopRatedTvShows(page: Int): RemoteTvShowResponse
    suspend fun getTvShowCast(tvShowId: Long): RemoteCastAndCrewResponse

    suspend fun getTvShowsByKeyword(keyword: String, page: Int, ): RemoteTvShowResponse

    suspend fun getTvShowDetailsById(tvShowId: Long): TvShowDetailsRemoteResponse

    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): EpisodeResponse
    suspend fun getEpisodeVideos(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): VideoResponse

    suspend fun getRatedTvShows(): RemoteTvShowResponse
    suspend fun setTvShowRate(rate: Int, tvShowId: Long): RatingResponse?
    suspend fun getTvShowsByGenreId(genreId: Long, page: Int): RemoteTvShowResponse

    suspend fun deleteTvShowRate(tvShowId: Long)

}