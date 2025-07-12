package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.repository.datasource.local.LocalTvShowDataSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategories
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class LocalTvShowDataSourceImpl(
    private val dao: TvShowDao
) : LocalTvShowDataSource {

    override suspend fun getTvShowsByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<TvShowWithCategories> {
        return dao.getTvShowsByKeywordAndSearchType(keyword, searchType)
    }

    override suspend fun addTvShowsWithSearchData(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String,
        searchType: SearchType
    ) {
        dao.insertTvShows(tvShows)

        val entries = tvShows.map { tvShow ->
            LocalSearchDto(
                searchKeyword = searchKeyword,
                searchType = searchType,
                expireDate = Clock.System.now().plus(1.hours)
            )
        }

        dao.insertSearchEntries(entries)
    }
}