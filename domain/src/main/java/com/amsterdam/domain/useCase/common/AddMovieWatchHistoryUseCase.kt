package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.MovieWatchHistory
import kotlinx.datetime.Clock

class AddMovieWatchHistoryUseCase(
    private val watchHistoryRepository: WatchHistoryRepository
) {
    suspend operator fun invoke(movieId: Long) {
        val movieWatchHistory = MovieWatchHistory(
            movieId = movieId,
            lastWatchedTime = Clock.System.now()
        )
        watchHistoryRepository.addMovieToWatchHistory(movieWatchHistory)
    }
}