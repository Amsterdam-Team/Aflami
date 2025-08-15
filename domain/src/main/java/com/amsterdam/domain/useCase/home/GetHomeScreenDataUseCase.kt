package com.amsterdam.domain.useCase.home

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.MovieGenre

class GetHomeScreenDataUseCase(
    private val getHomeTopRatedMoviesUseCase: GetHomeTopRatedMoviesUseCase,
    private val getHomeTopRatedTvShowsUseCase: GetHomeTopRatedTvShowsUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
) {

    suspend operator fun invoke(): HomeScreenData {
        return HomeScreenData(
            topRatedMovies = getHomeTopRatedMoviesUseCase(),
            topRatedTvShows = getHomeTopRatedTvShowsUseCase(),
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