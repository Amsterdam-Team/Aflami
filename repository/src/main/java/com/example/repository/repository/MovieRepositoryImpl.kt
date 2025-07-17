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
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> =
        recentSearchHandler.isRecentSearchExpired(keyword, SearchType.BY_KEYWORD)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getMoviesFromLocal(keyword, SearchType.BY_KEYWORD) }
            ?: recentSearchHandler.deleteRecentSearch(keyword, SearchType.BY_KEYWORD)
                .let { getMoviesByKeywordFromRemote(keyword, SearchType.BY_KEYWORD) }


    override suspend fun getMoviesByActor(actorName: String): List<Movie> =
        recentSearchHandler.isRecentSearchExpired(keyword = actorName, SearchType.BY_ACTOR)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getMoviesFromLocal(keyword = actorName, SearchType.BY_ACTOR) }
            ?: recentSearchHandler.deleteRecentSearch(actorName, SearchType.BY_ACTOR)
                .let { getMoviesByActorNameFromRemote(actorName, SearchType.BY_ACTOR) }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> =
        recentSearchHandler.isRecentSearchExpired(countryIsoCode, SearchType.BY_COUNTRY)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getMoviesFromLocal(countryIsoCode, SearchType.BY_COUNTRY) }
            ?: recentSearchHandler.deleteRecentSearch(countryIsoCode, SearchType.BY_COUNTRY)
                .let { getMoviesByCountryIsoCodeFromRemote(countryIsoCode, SearchType.BY_COUNTRY) }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String,
        searchType: SearchType
    ): List<Movie> =
        tryToExecute(
            function = { movieDataSource.getMoviesByKeyword(keyword) },
            onSuccess = { remoteMovies ->
                onSuccessGetMovies(remoteMovies, keyword, searchType)
            },
            onFailure = { aflamiException -> throw aflamiException }
        )


    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String,
        searchType: SearchType
    ): List<Movie> =
        tryToExecute(
            function = { movieDataSource.getMoviesByActorName(actorName) },
            onSuccess = { remoteMovies ->
                onSuccessGetMovies(remoteMovies, actorName, searchType)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String,
        searchType: SearchType
    ): List<Movie> =
        tryToExecute(
            function = { movieDataSource.getMoviesByCountryIsoCode(countryIsoCode) },
            onSuccess = { remoteMovies ->
                onSuccessGetMovies(remoteMovies, countryIsoCode, searchType)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )

    private suspend fun MovieRepositoryImpl.onSuccessGetMovies(
        remoteMovies: RemoteMovieResponse,
        actorName: String,
        searchType: SearchType
    ): List<Movie> =
        saveMoviesWithSearch(remoteMovies, actorName, searchType)
            .let { movieRemoteMapper.mapToMovies(remoteMovies) }

    private suspend fun getMoviesFromLocal(keyword: String, searchType: SearchType): List<Movie> =
        tryToExecute(
            function = {
                movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = keyword,
                    searchType = searchType
                )
            },
            onSuccess = { localMovies -> movieLocalMapper.mapToMovies(localMovies) },
            onFailure = { emptyList() },
        )

    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse,
        keyword: String,
        searchType: SearchType
    ) =
        tryToExecute(
            function = {
                movieLocalSource.addMoviesBySearchData(
                    movies = movieRemoteMapper.mapToLocalMovies(remoteMovies),
                    searchKeyword = keyword,
                    searchType = searchType,
                    expireDate = Clock.System.now()
                )
            },
            onSuccess = {},
            onFailure = {}
        )

}