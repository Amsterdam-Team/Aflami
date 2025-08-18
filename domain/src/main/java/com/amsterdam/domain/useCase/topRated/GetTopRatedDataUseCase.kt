package com.amsterdam.domain.useCase.topRated


import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow

class GetTopRatedDataUseCase(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
) {

    suspend operator fun invoke(
        page: Int = 1,
    ) : TopRatedScreenData {
        return TopRatedScreenData(
            topRatedMovies = getTopRatedMoviesUseCase(page),
            topRatedTvShows = getTopRatedTvShowsUseCase(page),
        )
    }

    data class TopRatedScreenData(
        val topRatedMovies: List<Movie>,
        val topRatedTvShows: List<TvShow>
    )
}