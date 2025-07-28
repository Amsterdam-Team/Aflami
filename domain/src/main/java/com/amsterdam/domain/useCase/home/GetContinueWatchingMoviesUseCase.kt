package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.Movie
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingMoviesUseCase (private val watchHistoryRepository: WatchHistoryRepository) {

    operator fun invoke(): Flow<List<Movie>> {
        return watchHistoryRepository.getContinueWatchingMovies()
    }

}