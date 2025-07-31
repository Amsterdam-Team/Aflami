package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.SearchTvShowCrossRefDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface TvShowDao {

    @Transaction
    @Query(
        """
        SELECT * FROM ${DatabaseConstants.TV_SHOW_TABLE} AS tv
        INNER JOIN (
          SELECT tvShowId, storedLanguage
          FROM ${DatabaseConstants.TV_SHOW_SEARCH_TABLE}
          WHERE searchKeyword = :keyword
            AND storedLanguage = :storedLanguage
          LIMIT :limit OFFSET :offset
        ) AS searchResult
          ON tv.tvShowId = searchResult.tvShowId
          AND tv.storedLanguage = searchResult.storedLanguage
        LEFT JOIN ${DatabaseConstants.TV_SHOW_CATEGORY_CROSS_REF_TABLE} AS categoryCrossRef
          ON tv.tvShowId = categoryCrossRef.tvShowId
        LEFT JOIN ${DatabaseConstants.TV_SHOW_CATEGORY_INTEREST_TABLE} AS genreInterest
          ON categoryCrossRef.categoryId = genreInterest.categoryId
        GROUP BY tv.tvShowId
        ORDER BY COALESCE(SUM(genreInterest.interestCount), 0) DESC
    """
    )
    suspend fun getTvShowsBySearchKeywordSortedByInterest(
        keyword: String,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<TvShowWithCategory>


    @Query(
        """
      SELECT * FROM ${DatabaseConstants.TV_SHOW_TABLE} AS tv
      LEFT JOIN ${DatabaseConstants.TV_SHOW_CATEGORY_CROSS_REF_TABLE} AS categoryCrossRef
        ON tv.tvShowId = categoryCrossRef.tvShowId
      LEFT JOIN ${DatabaseConstants.TV_SHOW_CATEGORY_INTEREST_TABLE} AS genreInterest
        ON categoryCrossRef.categoryId = genreInterest.categoryId
      WHERE tv.storedLanguage = :language
      GROUP BY tv.tvShowId
      ORDER BY COALESCE(SUM(genreInterest.interestCount), 0) DESC
      LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getTvShowsSortedByGenreInterest(
        language: String,
        limit: Int,
        offset: Int
    ): List<TvShowWithCategory>

    @Upsert
    suspend fun insertTvShow(tvShow: LocalTvShowDto)


    @Upsert
    suspend fun addAllTvShows(tvShows: List<LocalTvShowDto>)

    @Upsert
    suspend fun insertTvShowSearchEntries(entries: List<SearchTvShowCrossRefDto>)

    @Upsert
    suspend fun insertTvShowCategoryCrossRefs(crossRefs: List<TvShowCategoryCrossRefDto>)

    @Query(" SELECT * FROM ${DatabaseConstants.TV_SHOW_TABLE} WHERE tvShowId = :tvShowId and storedLanguage = :storedLanguage")
    suspend fun getTvShowById(tvShowId: Long, storedLanguage: String): LocalTvShowDto?
}