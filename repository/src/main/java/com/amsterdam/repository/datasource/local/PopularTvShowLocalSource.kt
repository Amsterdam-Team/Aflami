package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto
import kotlinx.datetime.Instant

interface PopularTvShowLocalSource {
    suspend fun addPopularTvShows(tvShows: List<LocalTvShowDto>)

    suspend fun deleteExpiredPopularTvShows(expirationTime: Instant, storedLanguage: String)
}