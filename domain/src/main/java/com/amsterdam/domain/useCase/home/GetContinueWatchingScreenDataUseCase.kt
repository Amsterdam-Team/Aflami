package com.amsterdam.domain.useCase.home

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import kotlinx.coroutines.flow.firstOrNull

class GetContinueWatchingScreenDataUseCase(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
) {

    suspend operator fun invoke(): ContinueWatchingScreenData {
        return ContinueWatchingScreenData(
            continueWatchingMovies = getContinueWatchingMoviesUseCase().firstOrNull() ?: emptyList(),
            continueWatchingTvShows = getContinueWatchingTvShowsUseCase().firstOrNull() ?: emptyList(),
        )
    }

    data class ContinueWatchingScreenData(
        val continueWatchingMovies: List<Movie>,
        val continueWatchingTvShows: List<TvShow>
    )
}