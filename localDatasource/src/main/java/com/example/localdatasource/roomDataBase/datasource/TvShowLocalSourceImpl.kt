package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.SearchType

class TvShowLocalSourceImpl(
    private val dao: TvShowDao
) : TvShowLocalSource {

    override suspend fun getTvShowsByKeywordAndSearchType(searchKeyword: String, searchType: SearchType): List<TvShowWithCategory> {
        return dao.getTvShowsBySearchKeyword(searchKeyword, searchType = searchType)
    }

    override suspend fun addTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String
    ) {

        dao.addAllTvShows(tvShows)

        val mappings = tvShows.map {
            LocalTvShowWithSearchDto(
                tvShowId = it.tvShowId,
                searchKeyword = searchKeyword
            )
        }

        dao.insertTvShowSearchMappings(mappings)
    }
}