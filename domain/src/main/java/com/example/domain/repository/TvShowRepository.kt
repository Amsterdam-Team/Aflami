package com.example.domain.repository

import com.example.entity.Actor
import com.example.entity.Episode
import com.example.entity.ProductionCompany
import com.example.entity.Review
import com.example.entity.Season
import com.example.entity.TvShow

interface TvShowRepository {
    suspend fun getTvShowByKeyword(keyword: String): List<TvShow>

    suspend fun getTvShowDetails(tvShowId: Long): TvShow

    suspend fun getTvShowCast(tvShowId: Long): List<Actor>

    suspend fun getTvShowSeasons(tvShowId: Long): List<Season>

    suspend fun getEpisodesBySeasonNumber(tvShowId: Long, seasonNumber: Int): List<Episode>

    suspend fun getTvShowReviews(tvShowId: Long): List<Review>

    suspend fun getSimilarTvShows(tvShowId: Long): List<TvShow>

    suspend fun getTvShowGallery(tvShowId: Long): List<String>

    suspend fun getProductionCompany(tvShowId: Long): List<ProductionCompany>
}