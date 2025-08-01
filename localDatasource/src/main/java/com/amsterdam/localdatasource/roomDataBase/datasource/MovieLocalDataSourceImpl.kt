package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.SearchMovieCrossRefDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.SearchType
import javax.inject.Inject


class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val interestDao: MovieCategoryInterestDao
) : MovieLocalSource {
    override suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<MovieWithCategories> {
        return movieDao
            .getMoviesBySearchKeywordSortedByInterest(
                keyword,
                searchType,
                storedLanguage,
                limit,
                offset
            )
    }

    @Transaction
    override suspend fun addMoviesBySearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType
    ) {
        movieDao.insertMovies(movies)
        val entries = movies.map { movie ->
            SearchMovieCrossRefDto(
                searchKeyword = searchKeyword,
                searchType = searchType,
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage,
            )
        }
        movieDao.insertSearchEntries(entries)
    }

    override suspend fun addMovieWithCategories(
        movie: LocalMovieDto,
        categories: List<LocalMovieCategoryDto>,
        storedLanguage: String
    ) {
        movieDao.insertMovie(movie)
        val movieCrossRefs = categories.map { category ->
            MovieCategoryCrossRefDto(
                movieId = movie.movieId,
                categoryId = category.categoryId,
                storedLanguage = storedLanguage
            )
        }
        movieDao.insertMovieCategoryCrossRefs(movieCrossRefs)
    }

    override suspend fun getMovieById(movieId: Long,storedLanguage: String): LocalMovieDto? {
        return movieDao.getMovieById(movieId,storedLanguage)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        interestDao.incrementInterest(categoryId)
    }

    override suspend fun insertMovie(movie: LocalMovieDto) {
        movieDao.insertMovie(movie)
    }
}
