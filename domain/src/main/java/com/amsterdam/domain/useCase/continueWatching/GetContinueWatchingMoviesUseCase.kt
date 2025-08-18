package com.amsterdam.domain.useCase.continueWatching

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.utils.MovieWatchHistory
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingMoviesUseCase (private val watchHistoryRepository: WatchHistoryRepository) {

    operator fun invoke(page: Int, pageSize: Int): Flow<List<MovieWatchHistory>> {
        return watchHistoryRepository.getContinueWatchingMovies(page,pageSize)
    }

}