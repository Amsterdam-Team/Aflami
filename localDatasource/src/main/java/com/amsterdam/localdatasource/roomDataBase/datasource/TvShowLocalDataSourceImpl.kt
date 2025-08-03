package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.PopularTvShowDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import kotlinx.datetime.Instant
import javax.inject.Inject

class TvShowLocalDataSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val tvShowCategoryInterestDao: TvShowCategoryInterestDao
) : TvShowLocalSource {
    @Transaction
    override suspend fun addTvShowWithCategories(
        tvShow: LocalTvShowDto,
        categoryIds: List<Long>,
        storedLanguage: String
    ) {
        tvShowDao.insertTvShow(tvShow)
        val tvShowCrossRefs = categoryIds.map { categoryId ->
            TvShowCategoryCrossRefDto(
                tvShowId = tvShow.tvShowId,
                categoryId = categoryId,
                storedLanguage = storedLanguage
            )
        }
        tvShowDao.insertTvShowCategoryCrossRefs(tvShowCrossRefs)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        tvShowCategoryInterestDao.incrementInterest(categoryId)
    }

    override suspend fun insertTvShow(tvShow: LocalTvShowDto) {
        tvShowDao.insertTvShow(tvShow)
    }

    override suspend fun getTvShowById(
        tvShowId: Long,
        storedLanguage: String
    ): LocalTvShowDto? {
        return tvShowDao.getTvShowById(tvShowId, storedLanguage)
    }

    override suspend fun getPopularTvShows(storedLanguage: String): List<TvShowWithCategory> {
        return tvShowDao.getPopularTvShows(storedLanguage)
    }

    override suspend fun getTopRatedTvShows(storedLanguage: String): List<LocalTvShowDto> {
        return tvShowDao.getTopRatedTvShows(storedLanguage)
    }

    @Transaction
    override suspend fun addPopularTvShows(tvShows: List<LocalTvShowDto>) {
        tvShowDao.insertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            PopularTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage,
                dateAdded = tvShow.insertedDate
            )
        }
        tvShowDao.insertPopularTvShows(entries)
    }

    override suspend fun deleteExpiredPopularTvShows(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        tvShowDao.deleteExpiredPopularTvShows(expirationTime, storedLanguage)
    }

    override suspend fun addTopRatedTvShows(tvShows: List<LocalTvShowDto>) {
        tvShowDao.insertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            TopRatedTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage
            )
        }
        tvShowDao.insertTopRatedTvShows(entries)
    }

    override suspend fun deleteExpiredTopRatedTvShows(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        tvShowDao.deleteExpiredTopRatedTvShows(expirationTime, storedLanguage)
    }
}