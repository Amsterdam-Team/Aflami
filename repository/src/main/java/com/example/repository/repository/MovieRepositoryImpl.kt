package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.local.relation.SearchWithMovies
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.SearchWithMoviesMapper
import com.example.repository.mapper.remote.RemoteMovieMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDatasource,
    private val movieLocalMapper: MovieLocalMapper,
    private val movieRemoteMapper: RemoteMovieMapper,
    private val recentSearchDatasource: LocalRecentSearchDataSource,
    private val searchWithMoviesMapper: SearchWithMoviesMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        return withContext(dispatcher) {
            val localMovies = recentSearchDatasource.getSearchByKeywordAndSearchType(
                keyword = keyword,
                searchType = SearchType.BY_KEYWORD
            )
            if (localMovies.movies.isNotEmpty()) handleLocalSearchResponse(localMovies)
            val remoteMovies = remoteMovieDataSource.getMoviesByKeyword(keyword)
            async {
                //todo insert keyword
                //    insert movie
//                localMovieDataSource.addAllMoviesWithSearchData(
//                    movies = ,
//                    searchKeyword = keyword,
//                    searchType = SearchType.BY_KEYWORD
//                )
            }
            movieRemoteMapper.mapResponseToDomain(remoteMovies)
        }
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        TODO("Not yet implemented")
    }

    private suspend fun handleLocalSearchResponse(searchWithMovies: SearchWithMovies) {
        if (isSearchExpired(searchWithMovies)) {
            recentSearchDatasource.deleteSearchByExpireDate(searchWithMovies.search.expireDate)
        }
    }

    private fun isSearchExpired(searchWithMovies: SearchWithMovies): Boolean {
        val currentTime = Clock.System.now()
        return currentTime > searchWithMovies.search.expireDate
    }
}