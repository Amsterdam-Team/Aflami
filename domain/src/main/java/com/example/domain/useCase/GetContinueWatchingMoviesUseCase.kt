package com.example.domain.useCase

import com.example.domain.repository.WatchHistoryRepository
import com.example.entity.Movie
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingMoviesUseCase(private val watchHistoryRepository: WatchHistoryRepository) {

    suspend operator fun invoke(): Flow<List<Movie>> {
        return watchHistoryRepository.getContinueWatchingMovies()
    }

}