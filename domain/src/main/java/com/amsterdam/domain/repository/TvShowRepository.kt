package com.amsterdam.domain.repository

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow

interface TvShowRepository {
    suspend fun getPopularTvShows(): List<TvShow>
    suspend fun getTopRatedTvShows(page: Int): List<TvShow>
    suspend fun getTvShowCast(tvShowId: Long): List<Actor>
    suspend fun getTvShowByKeyword(keyword: String, page: Int, tvShowsPerPage: Int): List<TvShow>
    suspend fun getTvShowDetails(tvShowId: Long): TvShowDetails
    suspend fun getTvShowSeasons(tvShowId: Long): List<Season>
    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): List<Episode>

    suspend fun setTvShowRate(rate: Int, tvShowId: Long)
    suspend fun getUserRatedTvShows(): List<UserRatedTvShow>
    suspend fun deleteTvShowRate(tvShowId: Long)
}