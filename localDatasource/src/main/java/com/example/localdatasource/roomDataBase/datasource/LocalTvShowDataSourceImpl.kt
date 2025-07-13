package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.repository.datasource.local.LocalTvShowDataSource
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto
import com.example.repository.dto.local.relation.TvShowWithCategory

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