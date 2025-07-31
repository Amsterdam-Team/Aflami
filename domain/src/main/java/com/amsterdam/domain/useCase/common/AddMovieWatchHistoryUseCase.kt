package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.repository.WatchHistoryRepository

class AddMovieWatchHistoryUseCase(
    private val watchHistoryRepository: WatchHistoryRepository
) {
    suspend operator fun invoke(movieId: Long) {
        watchHistoryRepository.addMovieToWatchHistory(movieId)
    }
}