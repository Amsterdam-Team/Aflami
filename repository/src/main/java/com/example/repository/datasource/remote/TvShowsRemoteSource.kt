package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteTvShowResponse

interface TvShowsRemoteSource {
    suspend fun getTvShowsByKeyword(
        keyword: String,
        page: Int,
    ): RemoteTvShowResponse
}
