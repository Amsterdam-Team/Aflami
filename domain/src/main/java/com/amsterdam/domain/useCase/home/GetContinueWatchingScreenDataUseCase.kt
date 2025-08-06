package com.amsterdam.domain.useCase.home

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetContinueWatchingScreenDataUseCase(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
) {

    operator fun invoke(page: Int = 1, pageSize: Int = 20): Flow<ContinueWatchingScreenData> {
        return combine(
            getContinueWatchingMoviesUseCase(page, pageSize / 2),
            getContinueWatchingTvShowsUseCase(page, pageSize / 2)
        ) { moviesWitchHistory, tvShowsWitchHistory ->
            ContinueWatchingScreenData(
                continueWatchingMovies = moviesWitchHistory,
                continueWatchingTvShows = tvShowsWitchHistory
            )
        }
    }

    data class ContinueWatchingScreenData(
        val continueWatchingMovies: List<MovieWatchHistory>,
        val continueWatchingTvShows: List<TvShowWatchHistory>
    )
}