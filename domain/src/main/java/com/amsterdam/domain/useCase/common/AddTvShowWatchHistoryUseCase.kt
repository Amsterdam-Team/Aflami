package com.amsterdam.domain.useCase.common

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.TvShowWatchHistory
import kotlinx.datetime.Clock

class AddTvShowWatchHistoryUseCase(
    private val watchHistoryRepository: WatchHistoryRepository
) {
    suspend operator fun invoke(tvShowId: Long) {
        val tvShowWatchHistory = TvShowWatchHistory(
            tvShowId = tvShowId,
            lastWatchedTime = Clock.System.now()
        )
        watchHistoryRepository.addTvShowToWatchHistory(tvShowWatchHistory)
    }
}