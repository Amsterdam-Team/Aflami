package com.example.domain.useCase

import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import kotlinx.coroutines.flow.firstOrNull

class GetHomeScreenDataUseCase(
    private val getTopRatedMoviesUseCase : GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase
    ) {

    suspend operator fun invoke(): HomeScreenData {
        return HomeScreenData(
            topRatedMovies = getTopRatedMoviesUseCase(),
            popularMovies = getPopularMoviesUseCase(),
            upComingMovies = getUpcomingMoviesUseCase(MovieGenre.ALL),
            continueWatchingMovies = getContinueWatchingMoviesUseCase.invoke().firstOrNull() ?: emptyList()
        )
    }
    data class HomeScreenData(
        val topRatedMovies: List<Movie>,
        val popularMovies: List<Movie>,
        val upComingMovies : List<Movie>,
        val continueWatchingMovies: List<Movie>
    )
}