package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.tryToExecute
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val movieLocalSource: MovieLocalSource,
    private val movieDataSource: MovieRemoteSource,
    private val movieLocalMapper: MovieLocalMapper,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        var movies: List<Movie> = emptyList()
        val searchType = SearchType.BY_KEYWORD
        if (!recentSearchHandler.isExpired(keyword, searchType)) {
            movies = getMoviesFromLocal(keyword, searchType)
        }
        if (movies.isNotEmpty()) return movies
        recentSearchHandler.deleteRecentSearch(keyword, searchType)
        return getMoviesByKeywordFromRemote(keyword, searchType)
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        var movies: List<Movie> = emptyList()
        val searchType = SearchType.BY_ACTOR
        if (!recentSearchHandler.isExpired(keyword = actorName, searchType)) {
            movies = getMoviesFromLocal(keyword = actorName, searchType)
        }
        if (movies.isNotEmpty()) return movies
        recentSearchHandler.deleteRecentSearch(actorName, searchType)
        return getMoviesByActorNameFromRemote(actorName, searchType)
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        var movies: List<Movie> = emptyList()
        val searchType = SearchType.BY_KEYWORD
        if (!recentSearchHandler.isExpired(countryIsoCode, searchType)) {
            movies = getMoviesFromLocal(countryIsoCode, searchType)
        }
        if (movies.isNotEmpty()) return movies
        recentSearchHandler.deleteRecentSearch(countryIsoCode, searchType)
        return getMoviesByCountryIsoCodeFromRemote(countryIsoCode, searchType)
    }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = {
                movieDataSource.getMoviesByKeyword(keyword)
            },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies,
                    keyword = keyword,
                    searchType = searchType,
                )
                movieRemoteMapper.mapToMovies(remoteMovies)
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }


    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = { movieDataSource.getMoviesByActorName(actorName) },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies = remoteMovies,
                    keyword = actorName,
                    searchType = searchType
                )
                movieRemoteMapper.mapToMovies(remoteMovies)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String,
        searchType: SearchType
    ): List<Movie> {
        return tryToExecute(
            function = { movieDataSource.getMoviesByCountryIsoCode(countryIsoCode) },
            onSuccess = { remoteMovies ->
                saveMoviesWithSearch(
                    remoteMovies = remoteMovies,
                    keyword = countryIsoCode,
                    searchType = searchType
                )
                movieRemoteMapper.mapToMovies(remoteMovies)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    private suspend fun getMoviesFromLocal(keyword: String, searchType: SearchType): List<Movie> {
        return tryToExecute(
            function = {
                movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = keyword,
                    searchType = searchType
                )
            },
            onSuccess = { localMovies -> movieLocalMapper.mapToMovies(localMovies) },
            onFailure = { emptyList() },
        )
    }

    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse,
        keyword: String,
        searchType: SearchType
    ) {
        val localMovies = movieRemoteMapper.mapToLocalMovies(remoteMovies)
        tryToExecute(
            function = {
                movieLocalSource.addMoviesBySearchData(
                    movies = localMovies,
                    searchKeyword = keyword,
                    searchType = searchType,
                    expireDate = Clock.System.now()
                )
            },
            onSuccess = {},
            onFailure = {}
        )
    }

}