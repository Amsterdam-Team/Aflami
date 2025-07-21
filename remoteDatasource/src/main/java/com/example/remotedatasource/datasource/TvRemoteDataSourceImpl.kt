package com.example.remotedatasource.datasource

import com.example.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse

class TvRemoteDataSourceImpl(
    private val tvShowsServiceProvider: TvShowsServiceProvider
) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return tvShowsServiceProvider.getTvShowsByKeyword(keyword, page)
    }
}