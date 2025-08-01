package com.amsterdam.domain.useCase.home


import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre

class GetTopRatedScreenDataUseCase(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
) {

    suspend operator fun invoke(
        page: Int = 1,
    ) : TopRatedScreenData{
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