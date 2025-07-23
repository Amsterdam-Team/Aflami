package com.example.domain.useCase

import com.example.entity.Movie
import com.example.entity.category.MovieGenre

class GetHomeScreenDataUseCase(
    private val getTopRatedMoviesUseCase : GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    ) {

    suspend operator fun invoke(): HomeScreenData {
        return HomeScreenData(
            topRatedMovies = getTopRatedMoviesUseCase(),
            popularMovies = getPopularMoviesUseCase(),
            upComingMovies = getUpcomingMoviesUseCase(MovieGenre.ALL)
        )
    }
    data class HomeScreenData(
        val topRatedMovies: List<Movie>,
        val popularMovies: List<Movie>,
        val upComingMovies : List<Movie>
    )
}