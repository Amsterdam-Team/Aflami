package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.TvShowWatchHistory
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingTvShowsUseCase(
    private val watchHistoryRepository: WatchHistoryRepository,
) {
    operator fun invoke(page: Int, pageSize: Int): Flow<List<TvShowWatchHistory>> {
        return watchHistoryRepository.getContinueWatchingTvShows(page,pageSize)
    }
}