package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.mapper.toDto
import com.amsterdam.repository.mapper.toDtoList
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toEntityList
import com.amsterdam.repository.mapper.toLocalDto
import com.amsterdam.repository.mapper.toLocalMovieDtoList
import com.amsterdam.repository.mapper.toLocalTvShowDtoList
import com.amsterdam.repository.mapper.toMovieEntityList
import com.amsterdam.repository.mapper.toMovieItemDto
import com.amsterdam.repository.mapper.toMovieUserRateEntityList
import com.amsterdam.repository.utils.getCachedOrRemoteData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class MovieRepositoryImpl @Inject constructor(
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val categoryRemoteDataSource: CategoryRemoteDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val preferences: AppLocalPreferences,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByKeywordFromRemote(keyword, page)
    }

    override suspend fun getMoviesByActor(
        actorName: String,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByActorNameFromRemote(actorName, page)
    }

    override suspend fun getMoviesByCountry(
        country: Country,
        page: Int,
        moviesPerPage: Int
    ): List<Movie> {
        return getMoviesByCountryIsoCodeFromRemote(country.countryIsoCode, page)
    }

    override suspend fun getActorsByMovieId(movieId: Long): List<Actor> {
        return movieRemoteDataSource.getCastByMovieId(movieId).cast.toEntityList()
    }

    override suspend fun getMovieDetailsById(movieId: Long): GetMovieDetailsUseCase.MovieDetails {
        return movieRemoteDataSource.getMovieDetailsById(movieId)
            .also {
                incrementUserInterestByMovie(it.genres)
                cacheWatchedMovie(it.toMovieItemDto())
            }.toEntity()
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return getCachedOrRemoteData<MovieWithCategories, MovieItemRemoteDto, Movie>(
            deleteExpired = ::deleteExpiredUpcomingMovies,
            getFromLocal = ::getUpcomingMoviesFromLocal,
            getFromRemote = ::getUpcomingMoviesFromRemote,
            saveRemoteToDatabase = ::saveUpcomingMovies,
            mapFromLocalToEntity = { it.toEntity() },
            mapFromRemoteToEntity = { it.toEntity(isPoster = false) }
        )
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return getCachedOrRemoteData<MovieWithCategories, MovieItemRemoteDto, Movie>(
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
        return if (page > 1) getTopRatedMoviesFromRemote(page).toMovieEntityList(isPoster = true)
        else getCachedOrRemoteData<MovieLocalDto, MovieItemRemoteDto, Movie>(
                deleteExpired = ::deleteExpiredTopRatedMovies,
                getFromLocal = ::getTopRatedMoviesFromLocal,
                getFromRemote = { getTopRatedMoviesFromRemote(page) },
                saveRemoteToDatabase = ::saveTopRatedMovies,
                mapFromLocalToEntity = MovieLocalDto::toEntity,
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

    private suspend fun cacheWatchedMovie(remoteMovieItemDto: MovieItemRemoteDto) {
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

    private suspend fun getUpcomingMoviesFromRemote(): List<MovieItemRemoteDto> {
        return movieRemoteDataSource.getUpcomingMovies().results
    }

    private suspend fun saveUpcomingMovies(remoteMovies: List<MovieItemRemoteDto>) {
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

    private suspend fun getPopularMoviesFromRemote(): List<MovieItemRemoteDto> {
        return movieRemoteDataSource.getPopularMovies().results
    }

    private suspend fun savePopularMovies(remoteMovies: List<MovieItemRemoteDto>) {
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

    private suspend fun getTopRatedMoviesFromLocal(): List<MovieLocalDto> {
        return movieLocalDataSource.getTopRatedMovies(
            preferences.getAppLanguage().first()
        )
    }

    private suspend fun getTopRatedMoviesFromRemote(page: Int): List<MovieItemRemoteDto> {
        return movieRemoteDataSource.getTopRatedMovies(page).results
    }

    private suspend fun saveTopRatedMovies(remoteMovies: List<MovieItemRemoteDto>) {
        saveMovieWithCategories(remoteMovies).also {
            movieLocalDataSource.upsertTopRatedMovies(
                remoteMovies.toLocalMovieDtoList(
                    storedLanguage = preferences.getAppLanguage().first()
                ),
            )
        }
    }

    private suspend fun getMoviesByKeywordFromRemote(keyword: String, page: Int): List<Movie> {
        return onSuccessGetRemoteMovies(movieRemoteDataSource.getMoviesByKeyword(keyword, page))
    }

    private suspend fun getMoviesByActorNameFromRemote(actorName: String, page: Int): List<Movie> {
        return movieRemoteDataSource.getActorIdsByName(actorName, page)
            .takeIf { actorIds -> actorIds.isNotEmpty() }
            ?.let { actorIds ->
                onSuccessGetRemoteMovies(movieRemoteDataSource.getMoviesByActorIds(actorIds, page))
            } ?: emptyList()
    }

    private suspend fun getMoviesByCountryIsoCodeFromRemote(
        countryIsoCode: String,
        page: Int
    ): List<Movie> {
        return onSuccessGetRemoteMovies(
            movieRemoteDataSource.getMoviesByCountryIsoCode(
                countryIsoCode,
                page
            )
        )
    }

    private fun onSuccessGetRemoteMovies(remoteMovies: MovieRemoteResponse): List<Movie> {
        return remoteMovies.results.toMovieEntityList()
    }

    private suspend fun saveMovieWithCategories(remoteMovies: List<MovieItemRemoteDto>) {
        cacheMovieCategoriesIfNotCached()
        remoteMovies.forEach { onSaveMovieWithCategories(it) }
    }

    private suspend fun onSaveMovieWithCategories(remoteMovie: MovieItemRemoteDto) {
        movieLocalDataSource.upsertMovieWithCategories(
            movie = remoteMovie.toLocalDto(storedLanguage = preferences.getAppLanguage().first()),
            categoryIds = remoteMovie.genreIds.map(Int::toLong),
            storedLanguage = preferences.getAppLanguage().first()
        )
    }

    override suspend fun setMovieRate(rate: Int, movieId: Long) {
        movieRemoteDataSource.setMovieRate(
            rate = rate.toFloat(), movieId = movieId
        )
    }

    override suspend fun getUserRatedMovies(): List<UserRatedMovie> {
        return movieRemoteDataSource.getRatedMovies().results.toMovieUserRateEntityList()
    }

    override suspend fun deleteMovieRate(movieId: Long) {
        movieRemoteDataSource.deleteMovieRate(movieId = movieId)
    }

    private suspend fun incrementUserInterestByMovie(remoteCategories: List<CategoryRemoteDto>) {
        remoteCategories
            .map(CategoryRemoteDto::id)
            .map { movieLocalDataSource.incrementGenreInterest(it.toLong()) }
    }

    private suspend fun cacheMovieCategoriesIfNotCached() {
        getMovieCategoriesFromLocal().takeIf { it.isNotEmpty() }
            ?: saveMovieCategoriesToDatabase(categoryRemoteDataSource.getMovieCategories())
    }

    private suspend fun getMovieCategoriesFromLocal(): List<MovieCategoryLocalDto> {
        return categoryLocalDataSource.getMovieCategories()
    }

    private suspend fun saveMovieCategoriesToDatabase(movieCategories: CategoryRemoteResponse) {
        categoryLocalDataSource.upsertMovieCategories(movieCategories.genres.toLocalTvShowDtoList())
    }
}