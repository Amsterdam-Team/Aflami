package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.TopRatedTvShowDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.TopRatedTvShowLocalSource
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import kotlinx.datetime.Instant
import javax.inject.Inject

class TopRatedTvShowLocalSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val topRatedTvShowDao: TopRatedTvShowDao
) : TopRatedTvShowLocalSource {
    override suspend fun addTopRatedTvShows(tvShows: List<LocalTvShowDto>) {
        tvShowDao.insertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            TopRatedTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage
            )
        }
        topRatedTvShowDao.insertTopRatedTvShows(entries)
    }

    override suspend fun deleteAllExpiredTopRatedTvShows(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        topRatedTvShowDao.deleteAllExpiredTopRatedTvShows(expirationTime, storedLanguage)
    }

}