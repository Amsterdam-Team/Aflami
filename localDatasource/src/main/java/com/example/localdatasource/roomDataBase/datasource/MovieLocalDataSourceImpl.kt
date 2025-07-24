package com.example.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.example.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.MovieCategoryCrossRefDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType


class MovieLocalDataSourceImpl(
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

    override suspend fun getMovieById(movieId: Long): LocalMovieDto {
        return movieDao.getMovieById(movieId)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        interestDao.incrementInterest(categoryId)
    }
}
