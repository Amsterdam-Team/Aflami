package com.amsterdam.domain.useCase.home


import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow

class GetTopRatedScreenDataUseCase(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
) {

    suspend operator fun invoke(): TopRatedScreenData {
        return TopRatedScreenData(
            topRatedMovies = getTopRatedMoviesUseCase(),
            topRatedTvShows = getTopRatedTvShowsUseCase(),
        )
    }

    data class TopRatedScreenData(
        val topRatedMovies: List<Movie>,
        val topRatedTvShows: List<TvShow>
    )
}