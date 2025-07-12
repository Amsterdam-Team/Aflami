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
import kotlinx.coroutines.launch
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
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)
            launch {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = keyword,
                    searchType = SearchType.BY_KEYWORD
                )
            }
            domainMovies
        }
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        return withContext(dispatcher) {
            val localMovies = recentSearchDatasource.getSearchByKeywordAndSearchType(
                keyword = actorName,
                searchType = SearchType.BY_ACTOR
            )
            if (localMovies.movies.isNotEmpty()) handleLocalSearchResponse(localMovies)
            val remoteMovies = remoteMovieDataSource.getMoviesByActorName(actorName)
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)
            launch {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = actorName,
                    searchType = SearchType.BY_ACTOR
                )
            }
            domainMovies
        }

    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        return withContext(dispatcher) {
            val localMovies = recentSearchDatasource.getSearchByKeywordAndSearchType(
                keyword = countryIsoCode,
                searchType = SearchType.BY_COUNTRY
            )

            if (localMovies.movies.isNotEmpty()) handleLocalSearchResponse(localMovies)
            val remoteMovies = remoteMovieDataSource.getMoviesByActorName(countryIsoCode)
            val domainMovies = movieRemoteMapper.mapResponseToDomain(remoteMovies)
            launch {
                localMovieDataSource.addAllMoviesWithSearchData(
                    movies = domainMovies.map { movieLocalMapper.mapToLocal(it) },
                    searchKeyword = countryIsoCode,
                    searchType = SearchType.BY_COUNTRY
                )
            }
            domainMovies

        }
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