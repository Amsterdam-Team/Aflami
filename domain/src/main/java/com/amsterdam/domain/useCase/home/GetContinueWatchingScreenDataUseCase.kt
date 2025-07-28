package com.amsterdam.domain.useCase.home

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingScreenDataUseCase(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
) {

    operator fun invoke(): ContinueWatchingScreenData {
        return ContinueWatchingScreenData(
            continueWatchingMovies = getContinueWatchingMoviesUseCase(),
            continueWatchingTvShows = getContinueWatchingTvShowsUseCase()
        )
    }

    data class ContinueWatchingScreenData(
        val continueWatchingMovies: Flow<List<Movie>>,
        val continueWatchingTvShows: Flow<List<TvShow>>
    )
}