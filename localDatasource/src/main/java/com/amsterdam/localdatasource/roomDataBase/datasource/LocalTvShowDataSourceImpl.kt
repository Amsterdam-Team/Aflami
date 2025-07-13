package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.LocalTvShowDataSource
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.LocalTvShowWithSearchDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory

class LocalTvShowDataSourceImpl(
    private val dao: TvShowDao
) : LocalTvShowDataSource {

    override suspend fun getTvShowsBySearchKeyword(searchKeyword: String): List<TvShowWithCategory> {
        return dao.getTvShowsBySearchKeyword(searchKeyword)
    }

    override suspend fun addAllTvShows(
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