package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.PopularTvShowDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Dao
interface TvShowDao {
    @Upsert
    suspend fun upsertTvShow(tvShow: TvShowLocalDto)

    @Upsert
    suspend fun upsertTvShows(tvShows: List<TvShowLocalDto>)

    @Query(" SELECT * FROM ${DatabaseConstants.TV_SHOW_TABLE} WHERE tvShowId = :tvShowId and storedLanguage = :storedLanguage")
    suspend fun getTvShowById(tvShowId: Long, storedLanguage: String): TvShowLocalDto?

    @Upsert
    suspend fun upsertTvShowCategoryCrossRefs(crossRefs: List<TvShowCategoryCrossRefDto>)

    @Query(
        """
        SELECT DISTINCT tv.* FROM ${DatabaseConstants.TV_SHOW_TABLE} AS tv
        INNER JOIN ${DatabaseConstants.POPULAR_TV_SHOW_TABLE} AS popularTv
        ON tv.tvShowId = popularTv.tvShowId
        LEFT JOIN ${DatabaseConstants.TV_SHOW_CATEGORY_CROSS_REF_TABLE} AS categoryCrossRef
        ON tv.tvShowId = categoryCrossRef.tvShowId
        WHERE tv.storedLanguage = popularTv.storedLanguage
        AND tv.storedLanguage = :storedLanguage
    """
    )
    suspend fun getPopularTvShows(storedLanguage: String): List<TvShowWithCategory>


    @Query(
        """
        SELECT * FROM ${DatabaseConstants.TV_SHOW_TABLE} AS tv
        INNER JOIN ${DatabaseConstants.TOP_RATED_TV_SHOW_TABLE} As topRatedTvShow
        ON tv.tvShowId = topRatedTvShow.tvShowId
        WHERE tv.storedLanguage = topRatedTvShow.storedLanguage
        AND tv.storedLanguage = :storedLanguage
    """
    )
    suspend fun getTopRatedTvShows(storedLanguage: String): List<TvShowLocalDto>

    @Upsert
    suspend fun upsertPopularTvShows(tvShows: List<PopularTvShowDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.POPULAR_TV_SHOW_TABLE}
            WHERE dateAdded < :expirationTime and storedLanguage = :storedLanguage
        """
    )
    suspend fun deleteExpiredPopularTvShows(expirationTime: Instant, storedLanguage: String)

    @Upsert
    suspend fun upsertTopRatedTvShows(tvShows: List<TopRatedTvShowDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.TOP_RATED_TV_SHOW_TABLE}
            WHERE dateAdded < :expirationTime AND storedLanguage = :storedLanguage
    """
    )
    suspend fun deleteExpiredTopRatedTvShows(expirationTime: Instant, storedLanguage: String)
}