package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteTvShowResponse

interface TvShowsRemoteSource {
    suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse
    suspend fun discoverTvShows(keyword: String, rating: Float, genreId: Int?): RemoteTvShowResponse
}