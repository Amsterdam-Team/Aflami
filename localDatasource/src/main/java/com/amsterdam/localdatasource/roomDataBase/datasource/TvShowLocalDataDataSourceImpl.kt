package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.PopularTvShowDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import kotlinx.datetime.Instant
import javax.inject.Inject

class TvShowLocalDataDataSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val tvShowCategoryInterestDao: TvShowCategoryInterestDao
) : TvShowLocalDataSource {
    @Transaction
    override suspend fun upsertTvShowWithCategories(
        tvShow: TvShowLocalDto,
        categoryIds: List<Long>,
        storedLanguage: String
    ) {
        tvShowDao.upsertTvShow(tvShow)
        val tvShowCrossRefs = categoryIds.map { categoryId ->
            TvShowCategoryCrossRefDto(
                tvShowId = tvShow.tvShowId,
                categoryId = categoryId,
                storedLanguage = storedLanguage
            )
        }
        tvShowDao.upsertTvShowCategoryCrossRefs(tvShowCrossRefs)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        tvShowCategoryInterestDao.incrementInterest(categoryId)
    }

    override suspend fun upsertTvShow(tvShow: TvShowLocalDto) {
        tvShowDao.upsertTvShow(tvShow)
    }

    override suspend fun getTvShowById(
        tvShowId: Long,
        storedLanguage: String
    ): TvShowLocalDto? {
        return tvShowDao.getTvShowById(tvShowId, storedLanguage)
    }

    override suspend fun getPopularTvShows(storedLanguage: String): List<TvShowWithCategory> {
        return tvShowDao.getPopularTvShows(storedLanguage)
    }

    override suspend fun getTopRatedTvShows(storedLanguage: String): List<TvShowLocalDto> {
        return tvShowDao.getTopRatedTvShows(storedLanguage)
    }

    @Transaction
    override suspend fun upsertPopularTvShows(tvShows: List<TvShowLocalDto>) {
        tvShowDao.upsertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            PopularTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage
            )
        }
        tvShowDao.upsertPopularTvShows(entries)
    }

    override suspend fun deleteExpiredPopularTvShows(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        tvShowDao.deleteExpiredPopularTvShows(expirationTime, storedLanguage)
    }

    override suspend fun upsertTopRatedTvShows(tvShows: List<TvShowLocalDto>) {
        tvShowDao.upsertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            TopRatedTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage
            )
        }
        tvShowDao.upsertTopRatedTvShows(entries)
    }

    override suspend fun deleteExpiredTopRatedTvShows(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        tvShowDao.deleteExpiredTopRatedTvShows(expirationTime, storedLanguage)
    }
}