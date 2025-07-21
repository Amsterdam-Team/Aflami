package com.example.remotedatasource.serviceProvider

import com.example.repository.dto.remote.RemoteTvShowResponse

interface TvShowsServiceProvider {
    suspend fun getTvShowsByKeyword(keyword: String, page: Int): RemoteTvShowResponse
}