package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService,
) : MovieRemoteDataSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): MovieRemoteResponse {
        return responseCall(
            execute = { movieApiService.getMoviesByKeyword(keyword, page) })
    }

    override suspend fun getMoviesByActorIds(actorIds: List<Int>, page: Int): MovieRemoteResponse {
        val actorIdsAsString = actorIds.joinToString(separator = "|")
        return responseCall(
            execute = { movieApiService.getMoviesByActorId(actorIdsAsString) })
    }

    override suspend fun getActorIdsByName(name: String, page: Int): List<Int> {
        return responseCall(
            execute = { movieApiService.getActorIdByName(name, page) })
            .actors
            .map { it.id }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): MovieRemoteResponse {
        return responseCall(execute = {
            movieApiService.getMoviesByCountryIsoCode(
                countryIsoCode,
                page
            )
        })
    }

    override suspend fun getCastByMovieId(movieId: Long): CastAndCrewRemoteResponse {
        return responseCall(
            execute = { movieApiService.getCastByMovieId(movieId) })
    }

    override suspend fun getMovieDetailsById(movieId: Long): MovieDetailsRemoteResponse {
        return responseCall(
            execute = { movieApiService.getMovieDetailsById(movieId) })
    }

    override suspend fun getPopularMovies(page: Int): MovieRemoteResponse {
        return responseCall(execute = { movieApiService.getPopularMovies(page) })
    }

    override suspend fun getUpcomingMovies(): MovieRemoteResponse {
        return responseCall(execute = { movieApiService.getUpcomingMovies() })
    }

    override suspend fun getTopRatedMovies(page: Int): MovieRemoteResponse {
        return responseCall(execute = { movieApiService.getTopRatedMovies(page) })
    }

    override suspend fun getMoviesByGenreIds(
        genresIds: List<Long>,
        page: Int
    ): MovieRemoteResponse {
        return responseCall(
            execute = { movieApiService.getMoviesByGenreIds(genresIds, page) })
    }

    override suspend fun getMoviesByGenreId(
        genreId: Long,
        page: Int
    ): MovieRemoteResponse {
        return responseCall(execute = {
            movieApiService.getMoviesByGenreIds(
                listOf(genreId),
                page
            )
        })
    }

    override suspend fun setMovieRate(rate: Float, movieId: Long): RatingRemoteResponse {
        return responseCall(execute = {
            movieApiService.postMovieRating(
                movieId = movieId,
                rate = rate
            )
        })
    }

    override suspend fun getRatedMovies(): MovieRemoteResponse {
        return responseCall(execute = { movieApiService.getRatedMovies() })
    }

    override suspend fun deleteMovieRate(movieId: Long) {
        responseCall(
            execute = { movieApiService.deleteMovieRate(movieId = movieId) })
    }

    override suspend fun getRandomMoviesWithReleaseDate(requiredMoviesNumber: Int): List<MovieItemRemoteDto> {
        return getRandomMovies(
            onFilter = ::onFilterHighQualityReleaseDate,
            requiredMoviesNumber = requiredMoviesNumber
        )
    }

    override suspend fun getRandomMoviesWithPoster(requiredMoviesNumber: Int): List<MovieItemRemoteDto> {
        return getRandomMovies(
            onFilter = ::onFilterHighQualityPoster,
            requiredMoviesNumber = requiredMoviesNumber
        )
    }

    private suspend fun getRandomMovies(
        onFilter: (MovieItemRemoteDto) -> Boolean,
        requiredMoviesNumber: Int
    ): List<MovieItemRemoteDto> {
        val totalPages = 500

        return (FIRST_PAGE..totalPages)
            .shuffled()
            .fold(emptyList()) { accumulatedMovies, page ->
                accumulatedMovies.takeIf { it.size >= requiredMoviesNumber }
                    ?: getPopularMoviesByPage(page)
                        .filter { movieDto -> onFilter(movieDto) }
                        .shuffled()
                        .distinctBy(MovieItemRemoteDto::id)
                        .plus(accumulatedMovies)
                        .take(requiredMoviesNumber)
            }
    }

    private suspend fun getPopularMoviesByPage(page: Int): List<MovieItemRemoteDto> {
        return getPopularMovies(page = page).results
    }

    private fun onFilterHighQualityReleaseDate(movieDto: MovieItemRemoteDto) =
        movieDto.releaseDate != null

    private fun onFilterHighQualityPoster(movieDto: MovieItemRemoteDto) =
        movieDto.posterPath != null

    private companion object {
        private const val FIRST_PAGE = 1
    }
}