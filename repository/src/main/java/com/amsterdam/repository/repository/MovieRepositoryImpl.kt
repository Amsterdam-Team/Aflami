package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.mapper.local.MovieGenreLocalMapper
import com.amsterdam.repository.mapper.local.MovieWithCategoriesLocalMapper
import com.amsterdam.repository.mapper.remote.CastRemoteMapper
import com.amsterdam.repository.mapper.remote.MovieDetailRemoteMapper
import com.amsterdam.repository.mapper.remote.MovieRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieGenreIdsRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.amsterdam.repository.utils.RecentSearchHandler
import com.amsterdam.repository.utils.getDeviceLanguage
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val movieGenreIdsRemoteLocalMapper: MovieGenreIdsRemoteLocalMapper,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val castRemoteMapper: CastRemoteMapper,
    private val movieDetailRemoteMapper: MovieDetailRemoteMapper,
    private val movieWithCategoriesLocalMapper: MovieWithCategoriesLocalMapper,
    private val movieRemoteLocalMapper: MovieRemoteLocalMapper,
    private val movieGenreLocalMapper: MovieGenreLocalMapper,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return categoryRepository.getMovieCategories().let {
            getCachedMovies(keyword, SearchType.BY_KEYWORD, page, moviesPerPage)
                ?: recentSearchHandler.deleteRecentSearch(
                    keyword, SearchType.BY_KEYWORD, getDeviceLanguage()
                ).let {
                    getMoviesByKeywordFromRemote(
                        keyword,
                        SearchType.BY_KEYWORD,
                        page,
                        moviesPerPage
                    )
                }
        }
    }

    override suspend fun getMoviesByActor(
        actorName: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return categoryRepository.getMovieCategories().let {
            getCachedMovies(actorName, SearchType.BY_ACTOR, page, moviesPerPage)
                ?: recentSearchHandler.deleteRecentSearch(
                    actorName, SearchType.BY_ACTOR, getDeviceLanguage()
                ).let {
                    getMoviesByActorNameFromRemote(
                        actorName,
                        SearchType.BY_ACTOR,
                        page,
                        moviesPerPage
                    )
                }
        }
    }

    override suspend fun getMoviesByCountry(
        country: Country,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return categoryRepository.getMovieCategories().let {
            getCachedMovies(country.countryIsoCode, SearchType.BY_COUNTRY, page, moviesPerPage)
                ?: recentSearchHandler.deleteRecentSearch(
                    country.countryIsoCode,
                    SearchType.BY_COUNTRY,
                    getDeviceLanguage()
                )
                    .let {
                        getMoviesByCountryIsoCodeFromRemote(
                            country.countryIsoCode,
                            SearchType.BY_COUNTRY,
                            page,
                            moviesPerPage
                        )
                    }
        }
    }

    override suspend fun getActorsByMovieId(movieId: Long): List<Actor> {
        return castRemoteMapper.toEntityList(movieRemoteDataSource.getCastByMovieId(movieId).cast)
    }

    override suspend fun getMovieDetailsById(movieId: Long): GetMovieDetailsUseCase.MovieDetails {
        return movieDetailRemoteMapper.toEntity(
            movieRemoteDataSource.getMovieDetailsById(movieId)
                .also {
                    incrementUserInterestByMovie(it.genres)
                    cacheWatchedMovie(movieDetailRemoteMapper.mapMovieDetailsToMovieItemDto(it))
                }
        )
    }

    private suspend fun cacheWatchedMovie(remoteMovieItemDto: RemoteMovieItemDto) {
        movieLocalSource.insertMovie(
            movieRemoteLocalMapper.toLocal(
                remote = remoteMovieItemDto, args = listOf(getDeviceLanguage())
            )
        )
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return movieRemoteMapper.toEntityList(movieRemoteDataSource.getUpcomingMovies().results)
    }

    override suspend fun getPopularMovies(): List<Movie> =
        movieRemoteMapper.toEntityList(movieRemoteDataSource.getPopularMovies().results)

    override suspend fun getTopRatedMovies(): List<Movie> =
        movieRemoteMapper.toEntityList(movieRemoteDataSource.getTopRatedMovies().results)

    private suspend fun getCachedMovies(
        keyword: String,
        searchType: SearchType,
        page: Int,
        moviesPerPage: Int
    ): List<Movie>? {
        return recentSearchHandler.isRecentSearchExpired(
            keyword,
            searchType,
            getDeviceLanguage()
        )
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getMoviesFromLocal(keyword, searchType, page, moviesPerPage) }
            ?.takeIf { movies -> movies.isNotEmpty() }
    }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String, searchType: SearchType, page: Int, moviesPerPage: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByKeyword(keyword, page),
            keyword,
            searchType,
            page,
            moviesPerPage
        )
    }

    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String, searchType: SearchType, page: Int, moviesPerPage: Int
    ): List<Movie> {
        return movieRemoteDataSource.getActorIdsByName(actorName, page).takeIf { actorIds ->
            actorIds.isNotEmpty()
        }?.let { actorIds ->
            onSuccessGetRemoteMovies(
                movieRemoteDataSource.getMoviesByActorIds(actorIds, page),
                actorName,
                searchType,
                page,
                moviesPerPage
            )
        } ?: emptyList()
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String, searchType: SearchType, page: Int, moviesPerPage: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByCountryIsoCode(countryIsoCode, page),
            countryIsoCode,
            searchType,
            page,
            moviesPerPage
        )
    }

    private suspend fun onSuccessGetRemoteMovies(
        remoteMovies: RemoteMovieResponse,
        keyword: String,
        searchType: SearchType,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return saveMovieWithCategories(remoteMovies).let {
            saveMoviesWithSearch(remoteMovies, keyword, searchType)
                .let { getMoviesFromLocal(keyword, searchType, page, moviesPerPage) }
                .takeIf { movies -> movies.isNotEmpty() }
                ?: movieRemoteMapper.toEntityList(remoteMovies.results)
        }
    }

    private suspend fun getMoviesFromLocal(
        keyword: String,
        searchType: SearchType,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return try {
            movieWithCategoriesLocalMapper.toEntityList(
                movieLocalSource.getMoviesByKeywordAndSearchType(
                    keyword = keyword,
                    searchType = searchType,
                    storedLanguage = getDeviceLanguage(),
                    limit = moviesPerPage,
                    offset = moviesPerPage * (page - 1)
                )
            )
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun saveMoviesWithSearch(
        remoteMovies: RemoteMovieResponse, keyword: String, searchType: SearchType
    ) {
        movieLocalSource.addMoviesBySearchData(
            movies = movieRemoteLocalMapper.toLocalList(
                remoteMovies.results,
                listOf(getDeviceLanguage())
            ),
            searchKeyword = keyword,
            searchType = searchType
        )
    }

    private suspend fun saveMovieWithCategories(remoteMovies: RemoteMovieResponse) {
        remoteMovies.results.forEach { onSaveMovieWithCategories(it) }
    }

    private suspend fun onSaveMovieWithCategories(remoteMovie: RemoteMovieItemDto) {
        movieLocalSource.addMovieWithCategories(
            movie = movieRemoteLocalMapper.toLocal(
                remoteMovie,
                listOf(getDeviceLanguage())
            ),
            categories = movieGenreIdsRemoteLocalMapper.toLocalList(
                remoteMovie.genreIds,
                listOf(getDeviceLanguage())
            ),
            storedLanguage = getDeviceLanguage()
        )
    }

    override suspend fun getMoviesByGenres(movieGenres: List<MovieGenre>): List<Movie> {
        return movieGenreLocalMapper.toDtoList(movieGenres).let { genresIds ->
            movieRemoteMapper.toEntityList(
                movieRemoteDataSource.getMoviesByGenreIds(
                    genresIds
                ).results
            )
        }
    }

    private suspend fun incrementUserInterestByMovie(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map { movieLocalSource.incrementGenreInterest(it.toLong()) }
    }


}