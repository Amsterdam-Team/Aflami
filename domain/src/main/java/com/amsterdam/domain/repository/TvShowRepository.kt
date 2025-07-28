package com.amsterdam.domain.repository

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow

interface TvShowRepository {
    suspend fun getPopularTvShows(): List<TvShow>
    suspend fun getTopRatedTvShows(): List<TvShow>
    suspend fun getTvShowByKeyword(keyword: String, page: Int, tvShowsPerPage: Int): List<TvShow>
    suspend fun getTvShowDetails(tvShowId: Long): TvShow
    suspend fun getTvShowCast(tvShowId: Long): List<Actor>
    suspend fun getTvShowSeasons(tvShowId: Long): List<Season>
    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): List<Episode>
    suspend fun getTvShowReviews(tvShowId: Long): List<Review>
    suspend fun getSimilarTvShows(tvShowId: Long): List<TvShow>
    suspend fun getTvShowGallery(tvShowId: Long): List<String>
    suspend fun getProductionCompany(tvShowId: Long): List<ProductionCompany>
}