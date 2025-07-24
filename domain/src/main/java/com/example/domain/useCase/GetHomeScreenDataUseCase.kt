package com.example.domain.useCase

import com.example.entity.Movie
import com.example.entity.TvShow
import com.example.entity.category.MovieGenre

class GetHomeScreenDataUseCase(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
) {

    suspend operator fun invoke(): HomeScreenData {
        return HomeScreenData(
            topRatedMovies = getTopRatedMoviesUseCase(),
            topRatedTvShows = getTopRatedTvShowsUseCase(),
            popularMovies = getPopularMoviesUseCase(),
            popularTvShows = getPopularTvShowsUseCase(),
            upComingMovies = getUpcomingMoviesUseCase(MovieGenre.ALL)
        )
    }

    data class HomeScreenData(
        val topRatedMovies: List<Movie>,
        val topRatedTvShows: List<TvShow>,
        val popularMovies: List<Movie>,
        val popularTvShows: List<TvShow>,
        val upComingMovies: List<Movie>
    )
}