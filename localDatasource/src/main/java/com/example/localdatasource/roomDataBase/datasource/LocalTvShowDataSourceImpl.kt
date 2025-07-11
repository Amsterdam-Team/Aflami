package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.repository.datasource.local.LocalTvShowDataSource
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto

class LocalTvShowDataSourceImpl(
    private val dao: TvShowDao
) : LocalTvShowDataSource {

    override suspend fun getTvShowsBySearchKeyword(searchKeyword: String): List<LocalTvShowDto> {
        return dao.getTvShowsBySearchKeyword(searchKeyword)
    }

    override suspend fun addAllTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String
    ) {

        dao.addAllTvShows(tvShows)

        val mappings = tvShows.map {
            LocalTvShowWithSearchDto(
                tvShowId = it.id,
                searchKeyword = searchKeyword
            )
        }

        dao.insertTvShowSearchMappings(mappings)
    }
}