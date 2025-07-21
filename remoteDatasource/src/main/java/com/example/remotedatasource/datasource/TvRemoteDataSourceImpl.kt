package com.example.remotedatasource.datasource

import com.example.remotedatasource.api.TvShowsApiService
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse

class TvRemoteDataSourceImpl(
    private val tvApiService: TvShowsApiService
) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return responseCall {
            tvApiService.getTvShowsByKeyword(keyword, page)
        }
    }
}
