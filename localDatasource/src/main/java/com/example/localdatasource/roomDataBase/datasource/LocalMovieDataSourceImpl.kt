package com.example.localdatasource.roomDataBase.datasource

import androidx.paging.PagingSource
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant


class LocalMovieDataSourceImpl(
    private val dao: MovieDao
) : LocalMovieDataSource {

    override suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<MovieWithCategories> {
        return dao.getMoviesByKeywordAndSearchType(keyword, searchType)
    }

    override suspend fun addAllMoviesWithSearchData(
        movies: List<LocalMovieDto>,
        searchKeyword: String,
        searchType: SearchType,
        expireDate: Instant
    ) {
        dao.insertMovies(movies)

        val entries = movies.map { movie ->
            SearchMovieCrossRefDto(
                searchKeyword = searchKeyword,
                searchType = searchType,
                movieId = movie.movieId
            )
        }

        dao.insertSearchEntries(entries)
    }

    override suspend fun getSearchMovieCrossRef(
        searchKeyword: String,
        searchType: SearchType
    ): List<SearchMovieCrossRefDto> {
        return dao.getSearchMoviesCrossRef(searchKeyword, searchType)
    }

    override fun getMoviesPagingSourceByKeywordAndType(
        keyword: String,
        searchType: SearchType
    ): PagingSource<Int, LocalMovieDto> {
        return dao.getMoviesPagingSourceByKeywordAndType(keyword, searchType)
    }

}
