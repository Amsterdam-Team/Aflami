package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.Episode

class GetEpisodesBySeasonNumberUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Long, seasonNumber: Int): List<Episode> {
        return tvShowRepository.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
    }
}