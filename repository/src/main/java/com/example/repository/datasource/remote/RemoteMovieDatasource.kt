package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.RemoteMovieSearchResponseDto

interface RemoteMovieDatasource {

    suspend fun getMoviesByKeyword(
        keyword: String,
        rating: Int,
        categoryId: Long?
    ): List<RemoteMovieResponse>

    suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieSearchResponseDto
}