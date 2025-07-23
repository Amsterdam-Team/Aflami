package com.example.domain.useCase

import com.example.entity.Movie
import com.example.entity.WatchHistory
import com.example.entity.category.MovieGenre

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
            continueWatchingMovies = getContinueWatchingMoviesUseCase()
        )
    }
    data class HomeScreenData(
        val topRatedMovies: List<Movie>,
        val popularMovies: List<Movie>,
        val upComingMovies : List<Movie>,
        val continueWatchingMovies: List<Movie>
    )
}