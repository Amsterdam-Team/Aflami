package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.SearchTvShowCrossRefDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import javax.inject.Inject

class TvShowLocalDataSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val tvShowCategoryInterestDao: TvShowCategoryInterestDao
) : TvShowLocalSource {

    override suspend fun getTvShowsBySearchKeywordSortedByInterest(
        searchKeyword: String,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<TvShowWithCategory> {
        return tvShowDao.getTvShowsBySearchKeywordSortedByInterest(
            keyword = searchKeyword,
            storedLanguage = storedLanguage,
            limit = limit,
            offset = offset
        )
    }

    override suspend fun addTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String,
        storedLanguage: String,
    ) {
        tvShowDao.addAllTvShows(tvShows)
        val entries = tvShows.map { tv ->
            SearchTvShowCrossRefDto(
                searchKeyword = searchKeyword,
                storedLanguage = tv.storedLanguage,
                tvShowId = tv.tvShowId
            )
        }
        tvShowDao.insertTvShowSearchEntries(entries)
    }

    override suspend fun addTvShowWithCategories(
        tvShow: LocalTvShowDto,
        categories: List<LocalTvShowCategoryDto>,
        storedLanguage: String
    ) {
        tvShowDao.insertTvShow(tvShow)
        val tvShowCrossRefs = categories.map { category ->
            TvShowCategoryCrossRefDto(
                tvShowId = tvShow.tvShowId,
                categoryId = category.categoryId,
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
}