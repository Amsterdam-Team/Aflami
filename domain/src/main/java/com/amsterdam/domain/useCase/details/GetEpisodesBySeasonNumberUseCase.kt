package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Episode

class GetEpisodesBySeasonNumberUseCase (
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Long, seasonNumber: Int): List<Episode> {
        return tvShowRepository.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
    }
}