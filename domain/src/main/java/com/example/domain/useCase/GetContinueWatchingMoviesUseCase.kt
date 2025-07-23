package com.example.domain.useCase

import com.example.domain.repository.WatchHistoryRepository
import com.example.entity.Movie

class GetContinueWatchingMoviesUseCase(private val watchHistoryRepository: WatchHistoryRepository) {

    suspend operator fun invoke(): List<Movie> {
        return watchHistoryRepository.getContinueWatchingMovies()
    }

}