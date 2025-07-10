package com.example.localdatasource.roomDatabase

import com.example.repository.datasource.local.LocalTvShowDataSource
import com.example.repository.dto.local.LocalTvShowDto

class LocalTvShowDataSourceImpl : LocalTvShowDataSource {

    override fun addAllTvShows(
        tvShows: List<LocalTvShowDto>,
        searchKeyword: String
    ) {

    }

    override fun getTvShowsBySearchKeyword(searchKeyword: String): List<LocalTvShowDto> {

    }

}