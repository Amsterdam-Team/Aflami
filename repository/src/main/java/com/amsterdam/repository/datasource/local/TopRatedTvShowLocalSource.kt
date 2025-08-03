package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalTvShowDto
import kotlinx.datetime.Instant

interface TopRatedTvShowLocalSource {
    suspend fun addTopRatedTvShows(tvShows: List<LocalTvShowDto>)
    suspend fun deleteAllExpiredTopRatedTvShows(expirationTime: Instant, storedLanguage: String)
}