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
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
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
import com.amsterdam.repository.utils.getCachedOrRemoteData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class MovieRepositoryImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val movieLocalSource: MovieLocalSource,
    private val movieRemoteDataSource: MovieRemoteSource,
    private val preferences: AppPreferences,
    private val movieRemoteMapper: MovieRemoteMapper,
    private val movieLocalMapper: MovieLocalMapper,
    private val castRemoteMapper: CastRemoteMapper,
    private val movieDetailRemoteMapper: MovieDetailRemoteMapper,
    private val movieRemoteLocalMapper: MovieRemoteLocalMapper,
    private val movieGenreLocalMapper: MovieGenreLocalMapper,
    private val movieWithCategoriesLocalMapper: MovieWithCategoriesLocalMapper,
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

    override suspend fun getMoviesByGenres(movieGenres: List<MovieGenre>): List<Movie> {
        return movieGenreLocalMapper.toDtoList(movieGenres).let { genresIds ->
            movieRemoteMapper.toEntityList(
                movieRemoteDataSource.getMoviesByGenreIds(
                    genresIds
                ).results
            )
        }
    }

    private suspend fun cacheWatchedMovie(remoteMovieItemDto: RemoteMovieItemDto) {
        movieLocalSource.insertMovie(
            movieRemoteLocalMapper.toLocal(
                remote = remoteMovieItemDto, args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    private suspend fun deleteExpiredUpcomingMovies() {
        movieLocalSource.deleteExpiredUpcomingMovies(
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
            movieLocalSource.addUpcomingMovies(
                movieRemoteLocalMapper.toLocalList(
                    remoteMovies,
                    listOf(preferences.getDeviceLanguage().first())
                )
            )
        }
    }

    private suspend fun deleteExpiredPopularMovies() {
        movieLocalSource.deleteExpiredPopularMovies(
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
            movieLocalSource.addPopularMovies(
                movieRemoteLocalMapper.toLocalList(
                    remoteMovies,
                    listOf(preferences.getDeviceLanguage().first())
                )
            )
        }
    }

    private suspend fun deleteExpiredTopRatedMovies() {
        movieLocalSource.deleteAllExpiredTopRatedMovies(
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
            movieLocalSource.addTopRatedMovies(
                movieRemoteLocalMapper.toLocalList(
                    remoteMovies,
                    listOf(preferences.getDeviceLanguage().first())
                )
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
        return movieRemoteMapper.toEntityList(remoteMovies.results)
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

    private suspend fun incrementUserInterestByMovie(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map { movieLocalSource.incrementGenreInterest(it.toLong()) }
    }

}