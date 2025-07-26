package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.entity.TvShow
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingTvShowsUseCase(
    private val watchHistoryRepository: WatchHistoryRepository,
) {
    operator fun invoke(): Flow<List<TvShow>> {
        return watchHistoryRepository.getContinueWatchingTvShows()
    }
}