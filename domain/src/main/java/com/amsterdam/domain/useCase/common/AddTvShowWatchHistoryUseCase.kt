package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.repository.WatchHistoryRepository

class AddTvShowWatchHistoryUseCase(
    private val watchHistoryRepository: WatchHistoryRepository
) {
    suspend operator fun invoke(tvShowId: Long) {
        watchHistoryRepository.addTvShowToWatchHistory(tvShowId)
    }
}