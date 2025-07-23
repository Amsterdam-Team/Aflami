package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.MovieCategoryCrossRefDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.DatabaseConstants
import com.example.repository.dto.local.utils.SearchType

@Dao
interface MovieDao {
    @Transaction
    @Query(
        """
        SELECT * FROM ${DatabaseConstants.MOVIE_TABLE} AS movie
        INNER JOIN (
          SELECT movieId, storedLanguage
          FROM ${DatabaseConstants.SEARCH_MOVIE_CROSS_REF_TABLE}
          WHERE searchKeyword = :keyword
            AND searchType = :searchType
            AND storedLanguage = :storedLanguage
          LIMIT :limit OFFSET :offset
        ) AS searchResult
          ON movie.movieId = searchResult.movieId
          AND movie.storedLanguage = searchResult.storedLanguage
        LEFT JOIN ${DatabaseConstants.MOVIE_CATEGORY_CROSS_REF_TABLE} AS categoryCrossRef
          ON movie.movieId = categoryCrossRef.movieId
        LEFT JOIN ${DatabaseConstants.MOVIE_CATEGORY_INTEREST_TABLE} AS genreInterest
          ON categoryCrossRef.categoryId = genreInterest.categoryId
        GROUP BY movie.movieId
        ORDER BY COALESCE(SUM(genreInterest.interestCount), 0) DESC
    """
    )
    suspend fun getMoviesBySearchKeywordSortedByInterest(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<MovieWithCategories>


    @Query(
        """
      SELECT * FROM ${DatabaseConstants.MOVIE_TABLE} AS movie
      LEFT JOIN ${DatabaseConstants.MOVIE_CATEGORY_CROSS_REF_TABLE} AS categoryCrossRef
        ON movie.movieId = categoryCrossRef.movieId
      LEFT JOIN ${DatabaseConstants.MOVIE_CATEGORY_INTEREST_TABLE} AS genreInterest
        ON categoryCrossRef.categoryId = genreInterest.categoryId
      WHERE movie.storedLanguage = :language
      GROUP BY movie.movieId
      ORDER BY COALESCE(SUM(genreInterest.interestCount), 0) DESC
      LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getMoviesSortedByGenreInterest(
        language: String,
        limit: Int,
        offset: Int
    ): List<MovieWithCategories>


    @Upsert
    suspend fun insertMovies(movies: List<LocalMovieDto>)

    @Upsert
    suspend fun insertMovie(movies: LocalMovieDto)

    @Upsert
    suspend fun insertSearchEntries(entries: List<SearchMovieCrossRefDto>)

    @Upsert
    suspend fun insertMovieCategoryCrossRefs(crossRefs: List<MovieCategoryCrossRefDto>)

    @Query(" SELECT * FROM ${DatabaseConstants.MOVIE_TABLE} WHERE movieId = :movieId")
    fun getMovieById(movieId: Long): LocalMovieDto
}
