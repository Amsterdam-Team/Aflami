package com.example.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.example.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.SearchTvShowCrossRefDto
import com.example.repository.dto.local.TvShowCategoryCrossRefDto
import com.example.repository.dto.local.relation.TvShowWithCategory

class TvShowLocalDataSourceImpl(
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

    @Transaction
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
}