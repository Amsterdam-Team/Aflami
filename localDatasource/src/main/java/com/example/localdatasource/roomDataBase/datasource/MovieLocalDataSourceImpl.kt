package com.example.localdatasource.roomDataBase.datasource

import com.example.entity.category.MovieGenre
import com.example.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant


class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao,
    private val interestDao: MovieCategoryInterestDao
) : MovieLocalSource {

    override suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<MovieWithCategories> {
        return movieDao.getMoviesByKeywordAndSearchType(keyword, searchType)
    }

    override suspend fun addMoviesBySearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType,
        expireDate: Instant
    ) {
        movieDao.insertMovies(movies)

        val entries = movies.map { movie ->
            SearchMovieCrossRefDto(
                searchKeyword = searchKeyword,
                searchType = searchType,
                movieId = movie.movieId
            )
        }

        movieDao.insertSearchEntries(entries)
    }

    override suspend fun getSearchMovieCrossRefs(
        searchKeyword: String,
        searchType: SearchType
    ): List<SearchMovieCrossRefDto> {
        return movieDao.getSearchMoviesCrossRef(searchKeyword, searchType)
    }

    override suspend fun getMovieById(movieId : Long): LocalMovieDto {
        return movieDao.getMovieById(movieId)
    }

    override suspend fun incrementGenreInterest(genre: MovieGenre) {
        interestDao.incrementInterest(genre)
    }

    override suspend fun getAllGenreInterests(): Map<MovieGenre, Int> {
        return interestDao.getAllInterests().associate { it.genre to it.interestCount }
    }

}
