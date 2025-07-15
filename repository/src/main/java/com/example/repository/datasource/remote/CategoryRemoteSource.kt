package com.example.repository.datasource.remote

import com.example.repository.dto.remote.RemoteCategoryResponse

interface CategoryRemoteSource {
    suspend fun getMovieCategories(): RemoteCategoryResponse
    suspend fun getTvShowCategories(): RemoteCategoryResponse
}