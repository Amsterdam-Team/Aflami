package com.example.domain.useCase

import com.example.entity.Movie
import com.example.entity.TvShow

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