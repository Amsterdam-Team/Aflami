package com.example.remotedatasource.serviceProvider.implementation

import com.example.remotedatasource.api.TvShowsApiService
import com.example.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.dto.remote.RemoteTvShowResponse

class TvShowsServiceProviderImpl(
    private val tvShowsApiService: TvShowsApiService
) : TvShowsServiceProvider {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse {
        return responseCall {
            tvShowsApiService.getTvShowsByKeyword(keyword, page)
        }
    }
}