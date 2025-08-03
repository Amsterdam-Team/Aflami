package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.datasource.local.PopularMovieLocalSource
import com.amsterdam.repository.datasource.local.TopRatedMovieLocalSource
import com.amsterdam.repository.datasource.local.UpcomingMovieLocalSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.mapper.local.MovieGenreLocalMapper
import com.amsterdam.repository.mapper.local.MovieLocalMapper
import com.amsterdam.repository.mapper.local.MovieWithCategoriesLocalMapper
import com.amsterdam.repository.mapper.remote.CastRemoteMapper
import com.amsterdam.repository.mapper.remote.MovieDetailRemoteMapper
import com.amsterdam.repository.mapper.remote.MovieRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.amsterdam.repository.utils.RecentSearchHandler
import com.amsterdam.repository.utils.getCachedOrRemoteData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class MovieRepositoryImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val movieLocalSource: MovieLocalSource,
    private val topRatedMovieLocalSource: TopRatedMovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val popularMovieLocalSource: PopularMovieLocalSource,
    private val upcomingMovieLocalSource: UpcomingMovieLocalSource,
    private val preferences: AppPreferences,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val movieLocalMapper: MovieLocalMapper,
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
                    keyword, SearchType.BY_KEYWORD, preferences.getDeviceLanguage().first()
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
                    actorName, SearchType.BY_ACTOR, preferences.getDeviceLanguage().first()
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
                    preferences.getDeviceLanguage().first()
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
                remote = remoteMovieItemDto, args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return getCachedOrRemoteData(
            deleteExpired = ::deleteExpiredUpcomingMovies,
            getFromLocal = ::getUpcomingMoviesFromLocal,
            getFromRemote = ::getUpcomingMoviesFromRemote,
            saveRemoteToDatabase = ::saveUpcomingMovies,
            mapFromLocalToEntity = movieWithCategoriesLocalMapper::toEntity,
            mapFromRemoteToEntity = { movieRemoteMapper.toEntity(it, isPoster = false) }
        )
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return getCachedOrRemoteData(
            deleteExpired = ::deleteExpiredPopularMovies,
            getFromLocal = ::getPopularMoviesFromLocal,
            getFromRemote = ::getPopularMoviesFromRemote,
            saveRemoteToDatabase = ::savePopularMovies,
            mapFromLocalToEntity = movieWithCategoriesLocalMapper::toEntity,
            mapFromRemoteToEntity = movieRemoteMapper::toEntity
        )
    }

    override suspend fun getTopRatedMovies(
        page: Int,
    ): List<Movie> {
        return getCachedOrRemoteData(
            deleteExpired = ::deleteExpiredTopRatedMovies,
            getFromLocal = ::getTopRatedMoviesFromLocal,
            getFromRemote = { getTopRatedMoviesFromRemote(page) },
            saveRemoteToDatabase = ::saveTopRatedMovies,
            mapFromLocalToEntity = movieLocalMapper::toEntity,
            mapFromRemoteToEntity = movieRemoteMapper::toEntity
        )
    }

    private suspend fun deleteExpiredUpcomingMovies() {
        upcomingMovieLocalSource.deleteExpiredUpcomingMovies(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getUpcomingMoviesFromLocal(): List<MovieWithCategories> {
        return movieLocalSource.getUpcomingMovies(
            preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getUpcomingMoviesFromRemote(): List<RemoteMovieItemDto> {
        return movieRemoteDataSource.getUpcomingMovies().results
    }

    private suspend fun saveUpcomingMovies(remoteMovies: List<RemoteMovieItemDto>) {
        saveMovieWithCategories(remoteMovies).also {
            upcomingMovieLocalSource.addUpcomingMovies(
                movieRemoteLocalMapper.toLocalList(
                    remoteMovies,
                    listOf(preferences.getDeviceLanguage().first())
                )
            )
        }
    }

    private suspend fun deleteExpiredPopularMovies() {
        popularMovieLocalSource.deleteExpiredPopularMovies(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getPopularMoviesFromLocal(): List<MovieWithCategories> {
        return movieLocalSource.getPopularMovies(
            preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getPopularMoviesFromRemote(): List<RemoteMovieItemDto> {
        return movieRemoteDataSource.getPopularMovies().results
    }

    private suspend fun savePopularMovies(remoteMovies: List<RemoteMovieItemDto>) {
        saveMovieWithCategories(remoteMovies).also {
            popularMovieLocalSource.addPopularMovies(
                movieRemoteLocalMapper.toLocalList(
                    remoteMovies,
                    listOf(preferences.getDeviceLanguage().first())
                )
            )
        }
    }

    private suspend fun deleteExpiredTopRatedMovies() {
        topRatedMovieLocalSource.deleteAllExpiredTopRatedMovies(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getTopRatedMoviesFromLocal(): List<LocalMovieDto> {
        return movieLocalSource.getTopRatedMovies(
            preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getTopRatedMoviesFromRemote(page: Int): List<RemoteMovieItemDto> {
        return movieRemoteDataSource.getTopRatedMovies(page).results
    }

    private suspend fun saveTopRatedMovies(remoteMovies: List<RemoteMovieItemDto>) {
        saveMovieWithCategories(remoteMovies).also {
            topRatedMovieLocalSource.addTopRatedMovies(
                movieRemoteLocalMapper.toLocalList(
                    remoteMovies,
                    listOf(preferences.getDeviceLanguage().first())
                )
            )
        }
    }


    private suspend fun getCachedMovies(
        keyword: String,
        searchType: SearchType,
        page: Int,
        moviesPerPage: Int
    ): List<Movie>? {
        return recentSearchHandler.isRecentSearchExpired(
            keyword,
            searchType,
            preferences.getDeviceLanguage().first()
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
        return saveMovieWithCategories(remoteMovies.results).let {
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
                    storedLanguage = preferences.getDeviceLanguage().first(),
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
                listOf(preferences.getDeviceLanguage().first())
            ),
            searchKeyword = keyword,
            searchType = searchType
        )
    }

    private suspend fun saveMovieWithCategories(remoteMovies: List<RemoteMovieItemDto>) {
        remoteMovies.forEach { onSaveMovieWithCategories(it) }
    }

    private suspend fun onSaveMovieWithCategories(remoteMovie: RemoteMovieItemDto) {
        categoryRepository.getMovieCategories().also {
            movieLocalSource.addMovieWithCategories(
                movie = movieRemoteLocalMapper.toLocal(
                    remoteMovie,
                    listOf(preferences.getDeviceLanguage().first())
                ),
                categoryIds = remoteMovie.genreIds.map(Int::toLong),
                storedLanguage = preferences.getDeviceLanguage().first()
            )
        }
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