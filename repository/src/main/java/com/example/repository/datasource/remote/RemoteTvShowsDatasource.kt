package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteTvShowResponse

interface RemoteTvShowsDatasource {
    suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse
}