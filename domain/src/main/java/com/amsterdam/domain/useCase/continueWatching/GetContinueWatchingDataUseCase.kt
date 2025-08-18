package com.amsterdam.domain.useCase.continueWatching

import com.amsterdam.domain.utils.MovieWatchHistory
import com.amsterdam.domain.utils.TvShowWatchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetContinueWatchingDataUseCase(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
) {

    operator fun invoke(page: Int = 1, pageSize: Int = 20): Flow<ContinueWatchingData> {
        return combine(
            getContinueWatchingMoviesUseCase(page, pageSize / 2),
            getContinueWatchingTvShowsUseCase(page, pageSize / 2)
        ) { moviesWitchHistory, tvShowsWitchHistory ->
            ContinueWatchingData(
                continueWatchingMovies = moviesWitchHistory,
                continueWatchingTvShows = tvShowsWitchHistory
            )
        }
    }

    data class ContinueWatchingData(
        val continueWatchingMovies: List<MovieWatchHistory>,
        val continueWatchingTvShows: List<TvShowWatchHistory>
    )
}