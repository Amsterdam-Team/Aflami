package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteMovieResponse

interface RemoteMovieDatasource {

    suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        rating: Int,
        categoryId: Long?
    ): List<RemoteMovieResponse>

}