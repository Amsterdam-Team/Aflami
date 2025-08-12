package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.mapper.local.toDto
import com.amsterdam.repository.mapper.local.toDtoList
import com.amsterdam.repository.mapper.local.toEntity
import com.amsterdam.repository.mapper.remote.toEntity
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.mapper.remote.toMovieDetailsEntity
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import com.amsterdam.repository.mapper.remote.toMovieItemDto
import com.amsterdam.repository.mapper.remote.toMovieUserRateEntityList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDto
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDtoList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalMovieDtoList
import com.amsterdam.repository.security.CryptoData
import com.amsterdam.repository.utils.getCachedOrRemoteData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class MovieRepositoryImpl @Inject constructor(
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val preferences: AppPreferences,
    val cryptoData: CryptoData,
) : MovieRepository {

    override suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByKeywordFromRemote(
            keyword,
            page
        )
    }

    override suspend fun getMoviesByActor(
        actorName: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByActorNameFromRemote(
            actorName,
            page
        )
    }

    override suspend fun getMoviesByCountry(
        country: Country,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByCountryIsoCodeFromRemote(
            country.countryIsoCode,
            page
        )
    }

    override suspend fun getActorsByMovieId(movieId: Long): List<Actor> {
        return movieRemoteDataSource.getCastByMovieId(movieId).cast.toEntityList()
    }

    override suspend fun getMovieDetailsById(movieId: Long): GetMovieDetailsUseCase.MovieDetails {
        val sessionId =
            cryptoData.decryptString(authenticationLocalDataSource.getCachedSessionId()) ?: ""

        return movieRemoteDataSource.getMovieDetailsById(movieId, sessionId)
            .also {
                incrementUserInterestByMovie(it.genres)
                cacheWatchedMovie(it.toMovieItemDto())
            }.toMovieDetailsEntity()
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return getCachedOrRemoteData<MovieWithCategories, RemoteMovieItemDto, Movie>(
            deleteExpired = ::deleteExpiredUpcomingMovies,
            getFromLocal = ::getUpcomingMoviesFromLocal,
            getFromRemote = ::getUpcomingMoviesFromRemote,
            saveRemoteToDatabase = ::saveUpcomingMovies,
            mapFromLocalToEntity = { it.toEntity() },
            mapFromRemoteToEntity = { it.toEntity(isPoster = false) }
        )
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return getCachedOrRemoteData<MovieWithCategories, RemoteMovieItemDto, Movie>(
            deleteExpired = ::deleteExpiredPopularMovies,
            getFromLocal = ::getPopularMoviesFromLocal,
            getFromRemote = ::getPopularMoviesFromRemote,
            saveRemoteToDatabase = ::savePopularMovies,
            mapFromLocalToEntity = { it.toEntity() },
            mapFromRemoteToEntity = { it.toEntity(isPoster = true) }
        )
    }

    override suspend fun getTopRatedMovies(
        page: Int,
    ): List<Movie> {
        return getCachedOrRemoteData<LocalMovieDto, RemoteMovieItemDto, Movie>(
            deleteExpired = ::deleteExpiredTopRatedMovies,
            getFromLocal = ::getTopRatedMoviesFromLocal,
            getFromRemote = { getTopRatedMoviesFromRemote(page) },
            saveRemoteToDatabase = ::saveTopRatedMovies,
            mapFromLocalToEntity = LocalMovieDto::toEntity,
            mapFromRemoteToEntity = { it.toEntity(isPoster = true) }
        )
    }

    override suspend fun getMoviesByGenres(movieGenres: List<MovieGenre>, page: Int): List<Movie> {
        return movieGenres.toDtoList().let { genresIds ->
            movieRemoteDataSource.getMoviesByGenreIds(
                genresIds,
                page
            ).results
                .toMovieEntityList()
        }
    }

    override suspend fun getMoviesByGenre(
        movieGenre: MovieGenre,
        page: Int
    ): List<Movie> {
        return movieGenre.toDto().let { genreId ->
            movieRemoteDataSource.getMoviesByGenreId(
                genreId,
                page
            ).results
                .toMovieEntityList()

        }
    }

    private suspend fun cacheWatchedMovie(remoteMovieItemDto: RemoteMovieItemDto) {
        movieLocalDataSource.upsertMovie(
            remoteMovieItemDto.toLocalDto(storedLanguage = preferences.getAppLanguage().first())
        )
    }

    private suspend fun deleteExpiredUpcomingMovies() {
        movieLocalDataSource.deleteExpiredUpcomingMovies(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getAppLanguage().first()
        )
    }

    private suspend fun getUpcomingMoviesFromLocal(): List<MovieWithCategories> {
        return movieLocalDataSource.getUpcomingMovies(
            preferences.getAppLanguage().first()
        )
    }

    private suspend fun getUpcomingMoviesFromRemote(): List<RemoteMovieItemDto> {
        return movieRemoteDataSource.getUpcomingMovies().results
    }

    private suspend fun saveUpcomingMovies(remoteMovies: List<RemoteMovieItemDto>) {
        saveMovieWithCategories(remoteMovies).also {
            movieLocalDataSource.upsertUpcomingMovies(
                remoteMovies.toLocalMovieDtoList(
                    isPoster = false,
                    preferences.getAppLanguage().first()
                )
            )
        }
    }

    private suspend fun deleteExpiredPopularMovies() {
        movieLocalDataSource.deleteExpiredPopularMovies(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getAppLanguage().first()
        )
    }

    private suspend fun getPopularMoviesFromLocal(): List<MovieWithCategories> {
        return movieLocalDataSource.getPopularMovies(
            preferences.getAppLanguage().first()
        )
    }

    private suspend fun getPopularMoviesFromRemote(): List<RemoteMovieItemDto> {
        return movieRemoteDataSource.getPopularMovies().results
    }

    private suspend fun savePopularMovies(remoteMovies: List<RemoteMovieItemDto>) {
        saveMovieWithCategories(remoteMovies).also {
            movieLocalDataSource.upsertPopularMovies(
                remoteMovies.toLocalMovieDtoList(
                    storedLanguage = preferences.getAppLanguage().first()
                ),
            )
        }
    }

    private suspend fun deleteExpiredTopRatedMovies() {
        movieLocalDataSource.deleteAllExpiredTopRatedMovies(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getAppLanguage().first()
        )
    }

    private suspend fun getTopRatedMoviesFromLocal(): List<LocalMovieDto> {
        return movieLocalDataSource.getTopRatedMovies(
            preferences.getAppLanguage().first()
        )
    }

    private suspend fun getTopRatedMoviesFromRemote(page: Int): List<RemoteMovieItemDto> {
        return movieRemoteDataSource.getTopRatedMovies(page).results
    }

    private suspend fun saveTopRatedMovies(remoteMovies: List<RemoteMovieItemDto>) {
        saveMovieWithCategories(remoteMovies).also {
            movieLocalDataSource.upsertTopRatedMovies(
                remoteMovies.toLocalMovieDtoList(
                    storedLanguage = preferences.getAppLanguage().first()
                ),
            )
        }
    }

    private suspend fun getMoviesByKeywordFromRemote(
        keyword: String, page: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByKeyword(keyword, page)
        )
    }

    private suspend fun getMoviesByActorNameFromRemote(
        actorName: String, page: Int
    ): List<Movie> {
        return movieRemoteDataSource.getActorIdsByName(actorName, page).takeIf { actorIds ->
            actorIds.isNotEmpty()
        }?.let { actorIds ->
            onSuccessGetRemoteMovies(
                movieRemoteDataSource.getMoviesByActorIds(actorIds, page)
            )
        } ?: emptyList()
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String, page: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByCountryIsoCode(countryIsoCode, page)
        )
    }

    private fun onSuccessGetRemoteMovies(
        remoteMovies: RemoteMovieResponse
    ): List<Movie> {
        return remoteMovies.results.toMovieEntityList()
    }

    private suspend fun saveMovieWithCategories(remoteMovies: List<RemoteMovieItemDto>) {
        cacheMovieCategoriesIfNotCached()
        remoteMovies.forEach { onSaveMovieWithCategories(it) }
    }

    private suspend fun onSaveMovieWithCategories(remoteMovie: RemoteMovieItemDto) {
        movieLocalDataSource.upsertMovieWithCategories(
            movie =
                remoteMovie.toLocalDto(storedLanguage = preferences.getAppLanguage().first()),
            categoryIds = remoteMovie.genreIds.map(Int::toLong),
            storedLanguage = preferences.getAppLanguage().first()
        )
    }

    override suspend fun setMovieRate(rate: Int, movieId: Long) {
        val sessionId =
            cryptoData.decryptString(authenticationLocalDataSource.getCachedSessionId()) ?: ""
        movieRemoteDataSource.setMovieRate(
            rate = rate.toFloat(),
            movieId = movieId,
            sessionId = sessionId
        )
    }

    override suspend fun getUserRatedMovies(): List<UserRatedMovie> {
        val sessionId =
            cryptoData.decryptString(authenticationLocalDataSource.getCachedSessionId()) ?: ""
        return movieRemoteDataSource.getRatedMovies(sessionId).results.toMovieUserRateEntityList()
    }

    override suspend fun deleteMovieRate(movieId: Long) {
        val sessionId =
            cryptoData.decryptString(authenticationLocalDataSource.getCachedSessionId()) ?: ""
        movieRemoteDataSource.deleteMovieRate(movieId = movieId, sessionId = sessionId)
    }

    private suspend fun incrementUserInterestByMovie(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map { movieLocalDataSource.incrementGenreInterest(it.toLong()) }
    }

    suspend fun cacheMovieCategoriesIfNotCached() {
        getMovieCategoriesFromLocal().takeIf { it.isNotEmpty() }
            ?: saveMovieCategoriesToDatabase(categoryRemoteSource.getMovieCategories())
    }

    private suspend fun getMovieCategoriesFromLocal(): List<LocalMovieCategoryDto> {
        return categoryLocalDataSource.getMovieCategories()
    }

    private suspend fun saveMovieCategoriesToDatabase(
        movieCategories: RemoteCategoryResponse
    ) {
        categoryLocalDataSource.upsertMovieCategories(
            movieCategories.genres.toLocalDtoList()
        )
    }

}