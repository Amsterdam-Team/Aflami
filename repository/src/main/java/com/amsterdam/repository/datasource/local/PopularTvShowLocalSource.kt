package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto

interface PopularTvShowLocalSource {
    suspend fun addPopularTvShows(tvShows: List<LocalTvShowDto>)

    suspend fun deleteExpiredPopularTvShows(expirationTime: Long, storedLanguage: String)
}