package com.example.localdatasource.roomDataBase.datasource

import com.example.entity.category.TvShowGenre
import com.example.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.example.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.SearchType

class TvShowLocalDataSourceImpl(
    private val tvShowDao: TvShowDao,
    private val tvShowCategoryInterestDao: TvShowCategoryInterestDao
) : TvShowLocalSource {

    override suspend fun getTvShowsByKeywordAndSearchType(searchKeyword: String, searchType: SearchType): List<TvShowWithCategory> {
        return tvShowDao.getTvShowsBySearchKeyword(searchKeyword, searchType = searchType)
    }

    override suspend fun addTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String
    ) {

        tvShowDao.addAllTvShows(tvShows)

        val mappings = tvShows.map {
            LocalTvShowWithSearchDto(
                tvShowId = it.tvShowId,
                searchKeyword = searchKeyword
            )
        }

        tvShowDao.insertTvShowSearchMappings(mappings)
    }

    override suspend fun incrementGenreInterest(genre: TvShowGenre) {
        tvShowCategoryInterestDao.incrementInterest(genre)
    }

    override suspend fun getAllGenreInterests(): Map<TvShowGenre, Int> {
        return tvShowCategoryInterestDao.getAllInterests().associate { it.genre to it.interestCount }
    }
}