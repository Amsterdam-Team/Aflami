package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.PopularTvShowsDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.datasource.local.PopularTvShowLocalSource
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.PopularTvShowDto
import javax.inject.Inject

class PopularTvShowLocalDataSourceImpl @Inject constructor(
    private val tvShowDao: TvShowDao,
    private val popularTvShowDao: PopularTvShowsDao
) : PopularTvShowLocalSource {
    @Transaction
    override suspend fun addPopularTvShows(tvShows: List<LocalTvShowDto>) {
        tvShowDao.insertTvShows(tvShows)
        val entries = tvShows.map { tvShow ->
            PopularTvShowDto(
                tvShowId = tvShow.tvShowId,
                storedLanguage = tvShow.storedLanguage,
                dateAdded = tvShow.insertedDate
            )
        }
        popularTvShowDao.insertPopularTvShows(entries)
    }

    override suspend fun deleteExpiredPopularTvShows(
        expirationTime: Long,
        storedLanguage: String
    ) {
        popularTvShowDao.deleteExpiredPopularTvShows(expirationTime, storedLanguage)
    }
}
