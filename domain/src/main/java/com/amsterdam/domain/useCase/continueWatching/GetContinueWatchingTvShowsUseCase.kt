package com.amsterdam.domain.useCase.continueWatching

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.utils.TvShowWatchHistory
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingTvShowsUseCase(
    private val watchHistoryRepository: WatchHistoryRepository,
) {
    operator fun invoke(page: Int, pageSize: Int): Flow<List<TvShowWatchHistory>> {
        return watchHistoryRepository.getContinueWatchingTvShows(page,pageSize)
    }
}