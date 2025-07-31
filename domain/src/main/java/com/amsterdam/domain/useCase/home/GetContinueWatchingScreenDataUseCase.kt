package com.amsterdam.domain.useCase.home

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShowWatchHistory
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingScreenDataUseCase(
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
    private val getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
) {

    operator fun invoke(page: Int = 1, pageSize: Int = 20): ContinueWatchingScreenData {
        return ContinueWatchingScreenData(
            continueWatchingMovies = getContinueWatchingMoviesUseCase(page,pageSize/2),
            continueWatchingTvShows = getContinueWatchingTvShowsUseCase(page,pageSize/2)
        )
    }

    data class ContinueWatchingScreenData(
        val continueWatchingMovies: Flow<List<MovieWatchHistory>>,
        val continueWatchingTvShows: Flow<List<TvShowWatchHistory>>
    )
}